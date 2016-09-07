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
@ContextConfiguration(locations = "classpath:beans-test.xml")
@Transactional
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DbUnitTestExecutionListener.class,
        TransactionalTestExecutionListener.class})
public class CommentDaoImplTest {

    private final Logger LOG = LogManager.getLogger(CommentDaoImplTest.class);
    private final Long C_ID_1 = 1L;
    private final Long N_ID_1 = 1L;
    private final Long N_ID_2 = 2L;
    private final String C_TEXT = "Something";
    private final String C_AUTHOR = "Author";
    private final Comment COMMENT = new Comment(C_ID_1,N_ID_1,C_TEXT,C_AUTHOR, Calendar.getInstance().getTime());

    @Autowired
    CommentDAO commentDAO;

    @Autowired
    NewsDAO newsDAO;

    @Test
    @DatabaseSetup(value = "classpath:dataset.xml", type = DatabaseOperation.CLEAN_INSERT)
    @DatabaseTearDown(value = "classpath:dataset.xml", type = DatabaseOperation.DELETE_ALL)
    public void testFindCommentsByNewsId() {

        try {
            assertTrue(2==commentDAO.findCommentsByNewsId(N_ID_2).size());
        } catch (DAOException e) {
            LOG.error(e);
        }

    }

    @Test
    @DatabaseSetup(value = "classpath:dataset.xml", type = DatabaseOperation.CLEAN_INSERT)
    @DatabaseTearDown(value = "classpath:dataset.xml", type = DatabaseOperation.DELETE_ALL)
    public void testFindAll(){
        try {
            assertTrue(3==commentDAO.findAll().size());
        } catch (DAOException e) {
            LOG.error(e);
        }
    }

    @Test
    @DatabaseSetup(value = "classpath:dataset.xml", type = DatabaseOperation.CLEAN_INSERT)
    @DatabaseTearDown(value = "classpath:dataset.xml", type = DatabaseOperation.DELETE_ALL)
    public void testFindDomainById(){

        try {
            assertEquals(C_TEXT,commentDAO.findDomainById(C_ID_1).getCommentText());
        } catch (DAOException e) {
            LOG.error(e);
        }
    }

    @Test
    @DatabaseSetup(value = "classpath:dataset.xml", type = DatabaseOperation.CLEAN_INSERT)
    @DatabaseTearDown(value = "classpath:dataset.xml", type = DatabaseOperation.DELETE_ALL)
    public void testDelete() {

        try {
            commentDAO.delete(C_ID_1);
            assertEquals(null,commentDAO.findDomainById(C_ID_1));
        } catch (DAOException e) {
            LOG.error(e);
        }
    }

    @Test
    @DatabaseSetup(value = "classpath:dataset.xml", type = DatabaseOperation.CLEAN_INSERT)
    @DatabaseTearDown(value = "classpath:dataset.xml", type = DatabaseOperation.DELETE_ALL)
    public void testCreate(){

        Long commentId;

        try {
            COMMENT.setNews(newsDAO.findDomainById(N_ID_1));
            commentId = commentDAO.create(COMMENT);
            assertEquals(COMMENT,commentDAO.findDomainById(commentId));
        } catch (DAOException e) {
            LOG.error(e);
        }
    }
}
