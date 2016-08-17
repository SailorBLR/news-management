package testcom.epam.hubarevich.dao.impl;

import com.epam.hubarevich.dao.exception.DAOException;
import com.epam.hubarevich.dao.impl.AuthorDAOImpl;
import com.epam.hubarevich.domain.Author;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

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
@DatabaseSetup(value = "classpath:dataset.xml", type = DatabaseOperation.CLEAN_INSERT)
@DatabaseTearDown(value = "classpath:dataset.xml", type = DatabaseOperation.DELETE_ALL)


public class AuthorDaoImplTest {
    private static final Logger LOG = LogManager.getLogger(AuthorDaoImplTest.class);

    @Autowired
    private AuthorDAOImpl authorDAO;

    @Test
    public void testCreate() {
        try {
            Author author = new Author();
            author.setAuthorName("Test");
            Assert.assertNotEquals(Long.valueOf(1L), authorDAO.create(author));
        } catch (DAOException e) {
            LOG.error(e);
        }
    }

    @Test
    public void testDelete() {
        try {
            authorDAO.delete(1L);
            Assert.assertEquals(authorDAO.findDomainById(1L), null);
        } catch (DAOException e) {
            LOG.error(e);
        }
    }

    @Test
    public void testUpdate() {
        String date = "2016-06-20 20:15:11";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            Author author = new Author(1L,"Ivan Ivanov");
            author.setExpired(sdf.parse(date));
            authorDAO.update(author);

            Assert.assertEquals(authorDAO.findDomainById(1L).getExpired().getTime(), author.getExpired().getTime());
        } catch (DAOException|ParseException e) {
            LOG.error(e);
        }
    }


    @Test
    public void testFindDomainById() {
        try {
            Author author = new Author();
            author.setAuthorId(1L);
            Assert.assertEquals(authorDAO.findDomainById(author.getAuthorId())
                    .getAuthorId(), author.getAuthorId());
        } catch (DAOException e) {
            LOG.error(e);
        }
    }

    @Test
    public void testFindAuthorByName() {
        try {
            Author author = new Author();
            author.setAuthorName("Ivan Ivanov");
            Assert.assertEquals(authorDAO.findAuthorByName(author.getAuthorName()).getAuthorName(),
                    author.getAuthorName());
        } catch (DAOException e) {
            LOG.error(e);
        }
    }

    @Test
    public void testFindAuthorByNewsId() {
        try {
            Author author = new Author(1L,"Ivan Ivanov");
            Assert.assertEquals(authorDAO.findAuthorByNewsId(1L).toString().trim(),
                    author.toString().trim());
        } catch (DAOException e) {
            LOG.error(e);
        }
    }

    @Test
    public void testFindAll() {
        try {
            Set<Author> authors = new HashSet<Author>();
            Author author = new Author(1L,"Ivan Ivanov");
            author.setExpired((new SimpleDateFormat("yyyy-MM-dd")).parse("2016-08-13"));
            Author author1 = new Author(2L,"Pavel Pavlov");
            Author author2 = new Author(3L,"Kto-to");
            authors.add(author);
            authors.add(author1);
            authors.add(author2);
            assertTrue(authorDAO.findAll().containsAll(authors));

        } catch (DAOException|ParseException e) {
            LOG.error(e);
        }
    }

    @Test
    public void testUnwireNewsAuthors(){
        try {
            authorDAO.unwireNewsAuthors(1L);
            Assert.assertEquals(authorDAO.findAuthorByNewsId(1L),null);
        } catch (DAOException e) {
            LOG.error(e);
        }
    }


    @Test
    public void testGetAvailableAuthors(){
        try {
            Set<Author> authors = new HashSet<Author>();
            Author author1 = new Author(2L,"Pavel Pavlov");
            Author author2 = new Author(3L,"Kto-to");
            authors.add(author1);
            authors.add(author2);
            assertTrue(authors.containsAll(authorDAO.getAvailableAuthors()));

        } catch (DAOException e) {
            LOG.error(e);
        }

    }
}
