package com.epam.hubarevich.dao;

import com.epam.hubarevich.dao.exception.DAOException;
import com.epam.hubarevich.domain.Author;


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


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
        "classpath:beans-test-hibernate.xml",
        "classpath:beans-test-eclipselink.xml"})
@Transactional
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DbUnitTestExecutionListener.class,
        TransactionalTestExecutionListener.class})

@ActiveProfiles("hibernate")
public class AuthorDaoImplTest{
    private final Logger LOG = LogManager.getLogger(AuthorDaoImplTest.class);
    private final String NAME_1 = "Ivan Ivanov";
    private final Long ID_1 = 1L;
    private final String NAME_2 = "Pavel Pavlov";
    private final Long ID_2 = 2L;
    private final String NAME_3 = "Kto-to";
    private final Long ID_3 = 3L;
    private final Author AUTHOR = new Author(ID_1,NAME_1);
    private final SimpleDateFormat SDF = new SimpleDateFormat("YYYY-MM-dd");
    private final String DATE = "2000-02-15";

    @Autowired
    AuthorDAO authorDAO;

    @Test
    public void testCreate(){
        try {
            authorDAO.create(AUTHOR);
            assertEquals(AUTHOR,authorDAO.findAuthorByName(NAME_1));
        } catch (DAOException e) {
            LOG.error(e);
        }
    }

    @Test
    @DatabaseSetup(value = "classpath:dataset.xml", type = DatabaseOperation.CLEAN_INSERT)
    @DatabaseTearDown(value = "classpath:dataset.xml", type = DatabaseOperation.DELETE_ALL)
    public void testFindAuthorByName(){

        try {
            assertEquals(AUTHOR.toString().trim(),authorDAO.findAuthorByName(NAME_1).toString().trim());
        } catch (DAOException e) {
            LOG.error(e);
        }
    }

    @Test
    @DatabaseSetup(value = "classpath:dataset.xml", type = DatabaseOperation.CLEAN_INSERT)
    @DatabaseTearDown(value = "classpath:dataset.xml", type = DatabaseOperation.DELETE_ALL)
    public void testFindAll(){
        try {
            List<Author> authors = new ArrayList<>();
            authors.add(new Author(ID_1,NAME_1));
            authors.add(new Author(ID_2,NAME_2));
            authors.add(new Author(ID_3,NAME_3));
            assertTrue(authorDAO.findAll().containsAll(authors));
        } catch (DAOException e) {
            LOG.error(e);
        }
    }

    @Test
    @DatabaseSetup(value = "classpath:dataset.xml", type = DatabaseOperation.CLEAN_INSERT)
    @DatabaseTearDown(value = "classpath:dataset.xml", type = DatabaseOperation.DELETE_ALL)
    public void testFindAuthorByNewsId() {

        try {
            assertEquals(AUTHOR.toString().trim(),authorDAO.findAuthorByNewsId(ID_1).toString().trim());
        } catch (DAOException e) {
            LOG.error(e);
        }

    }

    @Test
    @DatabaseSetup(value = "classpath:dataset.xml", type = DatabaseOperation.CLEAN_INSERT)
    @DatabaseTearDown(value = "classpath:dataset.xml", type = DatabaseOperation.DELETE_ALL)
    public void testFindDomainById() {
        try {
            assertEquals(AUTHOR.toString().trim(),authorDAO.findDomainById(ID_1).toString().trim());
        } catch (DAOException e) {
            LOG.error(e);
        }

    }

    @Test
    @DatabaseSetup(value = "classpath:dataset.xml", type = DatabaseOperation.CLEAN_INSERT)
    @DatabaseTearDown(value = "classpath:dataset.xml", type = DatabaseOperation.DELETE_ALL)
    public void testDelete() {

        try {
            authorDAO.delete(ID_1);
            assertEquals(null, authorDAO.findDomainById(ID_1));
        } catch (DAOException e) {
           LOG.error(e);
        }
    }


    @Test
    @DatabaseSetup(value = "classpath:dataset.xml", type = DatabaseOperation.CLEAN_INSERT)
    @DatabaseTearDown(value = "classpath:dataset.xml", type = DatabaseOperation.DELETE_ALL)
    public void testGetAvailableAuthors(){
        List<Author> authors = new ArrayList<>();
        authors.add(new Author(ID_2,NAME_2));
        authors.add(new Author(ID_3,NAME_3));

        try {
            assertTrue(authorDAO.getAvailableAuthors().containsAll(authors));
        } catch (DAOException e) {
            LOG.error(e);
        }
    }


    @Test
    @DatabaseSetup(value = "classpath:dataset.xml", type = DatabaseOperation.CLEAN_INSERT)
    @DatabaseTearDown(value = "classpath:dataset.xml", type = DatabaseOperation.DELETE_ALL)
    public void testUpdate(){
        AUTHOR.setExpired(null);
        AUTHOR.setAuthorName(NAME_2);
        try {
            authorDAO.update(AUTHOR);
            assertEquals(AUTHOR.toString().trim(),authorDAO.findDomainById(AUTHOR.getAuthorId()).toString().trim());
            assertEquals(AUTHOR.getExpired(),authorDAO.findDomainById(AUTHOR.getAuthorId()).getExpired());
        } catch (DAOException e) {
            LOG.error(e);
        }

        try {
            AUTHOR.setExpired(SDF.parse(DATE));
        } catch (ParseException e) {
            LOG.error(e);
        }
        try {
            authorDAO.update(AUTHOR);
            assertEquals(AUTHOR.getExpired(),authorDAO.findDomainById(AUTHOR.getAuthorId()).getExpired());
        }catch (DAOException e) {
            LOG.error(e);
        }
    }
}
