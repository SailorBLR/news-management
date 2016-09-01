package com.epam.hubarevich.dao;


import com.epam.hubarevich.dao.exception.DAOException;
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

import static org.junit.Assert.assertEquals;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:beans-test.xml")
@Transactional
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DbUnitTestExecutionListener.class,
        TransactionalTestExecutionListener.class})
public class CommentDaoImplTest {

    private final Logger LOG = LogManager.getLogger(CommentDaoImplTest.class);
    private final Long C_ID_1 = 1L;

    @Autowired
    CommentDAO commentDAO;
    @Test
    public void testFindCommentsByNewsId() throws Exception {

    }

    @Test
    public void testDeleteCommentsByNewsId() throws Exception {

    }

    @Test
    public void testFindAll() throws Exception {

    }

    @Test
    public void testFindDomainById() throws Exception {

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
    public void testCreate() throws Exception {

    }

    @Test
    public void testUpdate() throws Exception {

    }
}
