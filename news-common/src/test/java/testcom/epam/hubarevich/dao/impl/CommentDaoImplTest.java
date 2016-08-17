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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
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
@DatabaseSetup(value = "classpath:dataset.xml", type = DatabaseOperation.CLEAN_INSERT)
@DatabaseTearDown(value = "classpath:dataset.xml", type = DatabaseOperation.DELETE_ALL)
public class CommentDaoImplTest {
    private final Logger LOG = LogManager.getLogger(CommentDaoImplTest.class);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String creation = "2016-06-20 20:15:11";


    @Autowired
    CommentDAOImpl commentDao = new CommentDAOImpl();
    @Autowired
    NewsDAOImpl newsDAO = new NewsDAOImpl();

    @Transactional
    @Rollback(true)
    @Test
    public void testCreate() throws Exception {

        News news = new News();
        news.setTitle("T");
        news.setShortText("ST");
        news.setFullText("S");
        news.setNewsCreationDate(Calendar.getInstance().getTime());
        news.setNewsModificationDate(Calendar.getInstance().getTime());
        newsDAO.create(news);
        Comment comment = new Comment();
        comment.setNewsId(newsDAO.findAll().iterator().next().getNewsId());
        comment.setCommentText("Something");
        comment.setCommentAuthor("Anton");
        comment.setCommentCreationDate(sdf.parse(creation));
        Assert.assertNotEquals(Long.valueOf(0L), commentDao.create(comment));
    }


    @Test
    public void testDelete() {
        try {
            commentDao.delete(1L);
            Assert.assertEquals(commentDao.findDomainById(1L), null);
        } catch (DAOException e) {
            LOG.error(e);
        }
    }

    @Test
    public void testUpdate() {
        try {
            Comment comment = new Comment(1L, 1L, "SomethingElse", "Anton", sdf.parse(creation));
            commentDao.update(comment);
            Assert.assertEquals(commentDao.findDomainById(1L).getCommentCreationDate().getTime()
                    , comment.getCommentCreationDate().getTime());
        } catch (DAOException|ParseException e) {
            LOG.error(e);
        }
    }

    @Test
    public void testFindAll() {
        try {
            List<Comment> comments = new LinkedList<>();
            comments.add(commentDao.findDomainById(1L));
            comments.add(commentDao.findDomainById(2L));
            comments.add(commentDao.findDomainById(3L));
            assertTrue(commentDao.findAll().containsAll(comments));
        } catch (DAOException e) {
            LOG.error(e);
        }
    }

    @Test
    public void testFindCommentsByNewsId() {
        try {
            List<Comment> comments = new LinkedList<>();
            comments.add(commentDao.findDomainById(1L));
            comments.add(commentDao.findDomainById(3L));
            assertTrue(commentDao.findCommentsByNewsId(1L).containsAll(comments));
        } catch (DAOException e) {
            LOG.error(e);
        }
    }

    @Test
    public void testFindDomainById() {
        try {
            Comment comment = new Comment(1L, 1L, "Something", "Anton",
                    sdf.parse("2016-06-20 20:15:11"));
            assertTrue(comment.equals(commentDao.findDomainById(1L)));
        } catch (DAOException|ParseException e) {
            LOG.error(e);
        }

    }

    @Test
    public void testDeleteCommentsByNewsId() {
        List<Comment> comments;
        try {
            commentDao.deleteCommentsByNewsId(1L);
            comments = commentDao.findCommentsByNewsId(1L);
            assertTrue(comments.size() == 0);
        } catch (DAOException e) {
            LOG.error(e);
        }
    }
}

