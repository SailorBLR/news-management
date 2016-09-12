package com.epam.hubarevich.dao;


import com.epam.hubarevich.dao.exception.DAOException;
import com.epam.hubarevich.domain.Comment;
import com.epam.hubarevich.domain.News;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
        "classpath:beans-test-hibernate.xml",
        "classpath:beans-test-eclipselink.xml"})
@Transactional
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DbUnitTestExecutionListener.class,
        TransactionalTestExecutionListener.class})
@ActiveProfiles("elink")
public class CommentDaoImplTest {

    private final Logger LOG = LogManager.getLogger(CommentDaoImplTest.class);
    private final Long C_ID_1 = 1L;
    private final Long N_ID_1 = 1L;
    private final Long N_ID_2 = 2L;
    private final String C_TEXT = "Something";
    private final String C_AUTHOR = "Author";
    private final Comment COMMENT = new Comment(C_ID_1, N_ID_1, C_TEXT, C_AUTHOR, Calendar.getInstance().getTime());
    private final News NEWS = new News(1L, "Text", "TEXT", "Text", Calendar.getInstance().getTime(), Calendar.getInstance().getTime());

    @Autowired
    CommentDAO commentDAO;

    @Autowired
    NewsDAO newsDAO;

    @Test
    @DatabaseSetup(value = "classpath:dataset.xml", type = DatabaseOperation.CLEAN_INSERT)
    @DatabaseTearDown(value = "classpath:dataset.xml", type = DatabaseOperation.DELETE_ALL)
    public void testFindCommentsByNewsId() throws DAOException{

            assertTrue(2 == commentDAO.findCommentsByNewsId(N_ID_2).size());

    }

    @Test
    @DatabaseSetup(value = "classpath:dataset.xml", type = DatabaseOperation.CLEAN_INSERT)
    @DatabaseTearDown(value = "classpath:dataset.xml", type = DatabaseOperation.DELETE_ALL)
    public void testFindAll() throws DAOException{

            assertTrue(4 == commentDAO.findAll().size());
    }

    @Test
    @DatabaseSetup(value = "classpath:dataset.xml", type = DatabaseOperation.CLEAN_INSERT)
    @DatabaseTearDown(value = "classpath:dataset.xml", type = DatabaseOperation.DELETE_ALL)
    public void testFindDomainById() throws DAOException{

            assertEquals(C_TEXT, commentDAO.findDomainById(C_ID_1).getCommentText());

    }

   /* @Test
    @DatabaseSetup(value = "classpath:dataset.xml", type = DatabaseOperation.CLEAN_INSERT)
    @DatabaseTearDown(value = "classpath:dataset.xml", type = DatabaseOperation.DELETE_ALL)
    public void testDelete() {

        try {
            commentDAO.delete(C_ID_1);
            assertEquals(null,commentDAO.findDomainById(C_ID_1));
        } catch (DAOException e) {
            LOG.error(e);
        }
    }*/

    @Test
    public void testCreate() throws DAOException {

        newsDAO.create(NEWS);

        COMMENT.setNews(NEWS);

        int size = commentDAO.findAll().size();

        COMMENT.setCommentId(null);

        commentDAO.create(COMMENT);

        assertEquals(size + 1, commentDAO.findAll().size());
    }
}
