package testcom.epam.hubarevich.dao.impl;

import com.epam.hubarevich.dao.exception.DAOException;
import com.epam.hubarevich.dao.impl.CommentDAOImpl;
import com.epam.hubarevich.dao.impl.NewsDAOImpl;
import com.epam.hubarevich.domain.Comment;
import com.epam.hubarevich.domain.News;
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
import java.util.Calendar;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Test class for CommentDAO methods
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:beans-test.xml")
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DbUnitTestExecutionListener.class,
        TransactionalTestExecutionListener.class})

public class CommentDaoImplTest {
    private final Long C_ID_1 = 1L;
    private final Long N_ID_1 = 1L;
    private final int NUM_COMMENTS_2 = 2;
    private final int NUM_COMMENTS_3 = 3;
    private final String C_TEXT = "Something";
    private final String C_AUTHOR = "Author";
    private final String C_TEXT_2 = "Something_Else";
    private final Comment COMMENT = new Comment(C_ID_1, N_ID_1, C_TEXT, C_AUTHOR
            , Calendar.getInstance().getTime());
    private final News NEWS = new News(1L, "Text", "TEXT", "Text"
            , Calendar.getInstance().getTime()
            , Calendar.getInstance().getTime());



    @Autowired
    CommentDAOImpl commentDao = new CommentDAOImpl();
    @Autowired
    NewsDAOImpl newsDAO = new NewsDAOImpl();

    @Transactional
    @Test
    public void testCreate() throws DAOException, ParseException {
        long newsId = newsDAO.create(NEWS);
        COMMENT.setNewsId(newsId);
        Assert.assertNotEquals(Long.valueOf(0L), commentDao.create(COMMENT));
    }


    @Test
    @DatabaseSetup(value = "classpath:dataset.xml", type = DatabaseOperation.CLEAN_INSERT)
    @DatabaseTearDown(value = "classpath:dataset.xml", type = DatabaseOperation.DELETE_ALL)
    public void testDelete() throws DAOException {
        commentDao.delete(C_ID_1);
        Assert.assertEquals(null, commentDao.findDomainById(N_ID_1));
    }

    @Test
    @DatabaseSetup(value = "classpath:dataset.xml", type = DatabaseOperation.CLEAN_INSERT)
    @DatabaseTearDown(value = "classpath:dataset.xml", type = DatabaseOperation.DELETE_ALL)
    public void testUpdate() throws DAOException {
        COMMENT.setCommentText(C_TEXT_2);
        commentDao.update(COMMENT);
        Assert.assertEquals(COMMENT.getCommentText(),
                commentDao.findDomainById(C_ID_1).getCommentText());
    }

    @Test
    @DatabaseSetup(value = "classpath:dataset.xml", type = DatabaseOperation.CLEAN_INSERT)
    @DatabaseTearDown(value = "classpath:dataset.xml", type = DatabaseOperation.DELETE_ALL)
    public void testFindAll() throws DAOException {
        assertTrue(NUM_COMMENTS_3 == commentDao.findAll().size());
    }

    @Test
    @DatabaseSetup(value = "classpath:dataset.xml", type = DatabaseOperation.CLEAN_INSERT)
    @DatabaseTearDown(value = "classpath:dataset.xml", type = DatabaseOperation.DELETE_ALL)
    public void testFindCommentsByNewsId() throws DAOException {
        assertTrue(NUM_COMMENTS_2 == commentDao.findCommentsByNewsId(N_ID_1).size());
    }

    @Test
    public void testFindDomainById() throws DAOException {
        long newsId = newsDAO.create(NEWS);
        COMMENT.setNewsId(newsId);
        COMMENT.setCommentId(commentDao.create(COMMENT));
        assertTrue(COMMENT.equals(commentDao.findDomainById(COMMENT.getCommentId())));
    }

    @Test
    @DatabaseSetup(value = "classpath:dataset.xml", type = DatabaseOperation.CLEAN_INSERT)
    @DatabaseTearDown(value = "classpath:dataset.xml", type = DatabaseOperation.DELETE_ALL)
    public void testDeleteCommentsByNewsId() throws DAOException {
        List<Comment> comments;
        commentDao.deleteCommentsByNewsId(N_ID_1);
        comments = commentDao.findCommentsByNewsId(N_ID_1);
        assertTrue( 0 == comments.size());
    }
}

