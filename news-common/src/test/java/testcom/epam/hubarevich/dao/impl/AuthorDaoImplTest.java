package testcom.epam.hubarevich.dao.impl;

import com.epam.hubarevich.dao.exception.DAOException;
import com.epam.hubarevich.dao.impl.AuthorDAOImpl;
import com.epam.hubarevich.domain.Author;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertTrue;

/**
 * Test Junit for Author DAO methods
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:beans-test.xml")
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DbUnitTestExecutionListener.class,
        TransactionalTestExecutionListener.class})

public class AuthorDaoImplTest {
    private final String NAME_1 = "Ivan Ivanov";
    private final Long ID_1 = 1L;
    private final String NAME_2 = "Pavel Pavlov";
    private final Long ID_2 = 2L;
    private final String NAME_3 = "Kto-to";
    private final Long ID_3 = 3L;
    private final Author AUTHOR = new Author(ID_1, NAME_1);
    private final SimpleDateFormat SDF = new SimpleDateFormat("YYYY-MM-dd");
    private final String DATE = "2000-02-15";

    @Autowired
    private AuthorDAOImpl authorDAO;

    @Test
    @Transactional
    public void testCreate() throws DAOException {
        Assert.assertNotEquals(ID_1, authorDAO.create(AUTHOR));
    }

    @Test
    @Transactional
    public void testDelete() throws DAOException {
        authorDAO.create(AUTHOR);
        authorDAO.delete(ID_1);
        Assert.assertEquals(null,authorDAO.findDomainById(ID_1));
    }

    @Test
    @DatabaseSetup(value = "classpath:dataset.xml", type = DatabaseOperation.CLEAN_INSERT)
    @DatabaseTearDown(value = "classpath:dataset.xml", type = DatabaseOperation.DELETE_ALL)
    public void testUpdate() throws DAOException, ParseException {
        AUTHOR.setExpired(SDF.parse(DATE));
        authorDAO.update(AUTHOR);
        Assert.assertEquals(AUTHOR.getExpired().getTime()
                ,authorDAO.findDomainById(ID_1).getExpired().getTime());
    }


    @Test
    @DatabaseSetup(value = "classpath:dataset.xml", type = DatabaseOperation.CLEAN_INSERT)
    @DatabaseTearDown(value = "classpath:dataset.xml", type = DatabaseOperation.DELETE_ALL)
    public void testFindDomainById() throws DAOException {
        Assert.assertEquals(AUTHOR.getAuthorId(), authorDAO.findDomainById(ID_1).getAuthorId());
    }

    @Test
    @DatabaseSetup(value = "classpath:dataset.xml", type = DatabaseOperation.CLEAN_INSERT)
    @DatabaseTearDown(value = "classpath:dataset.xml", type = DatabaseOperation.DELETE_ALL)
    public void testFindAuthorByName() throws DAOException {

        Assert.assertEquals(NAME_1,
                authorDAO.findAuthorByName(NAME_1).getAuthorName());

    }

    @Test
    @DatabaseSetup(value = "classpath:dataset.xml", type = DatabaseOperation.CLEAN_INSERT)
    @DatabaseTearDown(value = "classpath:dataset.xml", type = DatabaseOperation.DELETE_ALL)
    public void testFindAuthorByNewsId() throws DAOException {

        Assert.assertEquals(AUTHOR.toString().trim(),
                authorDAO.findAuthorByNewsId(ID_1).toString().trim());
    }

    @Test
    @DatabaseSetup(value = "classpath:dataset.xml", type = DatabaseOperation.CLEAN_INSERT)
    @DatabaseTearDown(value = "classpath:dataset.xml", type = DatabaseOperation.DELETE_ALL)
    public void testFindAll() throws DAOException, ParseException {

        Set<Author> authors = new HashSet<>();
        Author author1 = new Author(ID_2, NAME_2);
        Author author2 = new Author(ID_3, NAME_3);
        Author author = authorDAO.findDomainById(ID_1);
        authors.add(author);
        authors.add(author1);
        authors.add(author2);
        assertTrue(authors.containsAll(authorDAO.findAll()));
    }

    @Test
    @DatabaseSetup(value = "classpath:dataset.xml", type = DatabaseOperation.CLEAN_INSERT)
    @DatabaseTearDown(value = "classpath:dataset.xml", type = DatabaseOperation.DELETE_ALL)
    public void testUnwireNewsAuthors() throws DAOException {
        authorDAO.unwireNewsAuthors(ID_1);
        Assert.assertEquals(null, authorDAO.findAuthorByNewsId(ID_1));
    }


    @Test
    @DatabaseSetup(value = "classpath:dataset.xml", type = DatabaseOperation.CLEAN_INSERT)
    @DatabaseTearDown(value = "classpath:dataset.xml", type = DatabaseOperation.DELETE_ALL)
    public void testGetAvailableAuthors() throws DAOException {

        Set<Author> authors = new HashSet<>();
        Author author1 = new Author(ID_2, NAME_2);
        Author author2 = new Author(ID_3, NAME_3);
        authors.add(author1);
        authors.add(author2);
        assertTrue(authors.containsAll(authorDAO.getAvailableAuthors()));
    }
}
