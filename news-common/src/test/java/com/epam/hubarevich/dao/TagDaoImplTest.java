package com.epam.hubarevich.dao;


import com.epam.hubarevich.dao.exception.DAOException;
import com.epam.hubarevich.domain.Tag;
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

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:beans-test.xml")
@Transactional
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DbUnitTestExecutionListener.class,
        TransactionalTestExecutionListener.class})
public class TagDaoImplTest {

    private final Logger LOG = LogManager.getLogger(TagDaoImplTest.class);
    private final String TAG_NAME = "Religion";
    private final String NEW_TAG_NAME = "New Religion";
    private final Long TAG_ID_1 = 1L;
    private final Long TAG_ID_2 = 2L;
    private final Long NEWS_ID = 1L;
    private final Tag TAG = new Tag(TAG_ID_1, TAG_NAME);
    private final int TAGS_SIZE = 3;

    @Autowired
    TagDAO tagDAO;
    @Autowired
    NewsDAO newsDAO;


    @Test
    @DatabaseSetup(value = "classpath:dataset.xml", type = DatabaseOperation.CLEAN_INSERT)
    @DatabaseTearDown(value = "classpath:dataset.xml", type = DatabaseOperation.DELETE_ALL)
    public void testGetTagsByNewsId() {
        List<Tag> tags = new ArrayList<>();
        try {
            tags.add(tagDAO.findDomainById(TAG_ID_1));
            tags.add(tagDAO.findDomainById(TAG_ID_2));
            assertTrue(tagDAO.getTagsByNewsId(NEWS_ID).containsAll(tags));
        } catch (DAOException e) {
            LOG.error(e);
        }

    }

    @Test
    @DatabaseSetup(value = "classpath:dataset.xml", type = DatabaseOperation.CLEAN_INSERT)
    @DatabaseTearDown(value = "classpath:dataset.xml", type = DatabaseOperation.DELETE_ALL)
    public void testFindAll() {
        try {
            assertEquals(TAGS_SIZE, tagDAO.findAll().size());
        } catch (DAOException e) {
            LOG.error(e);
        }
    }

    @Test
    @DatabaseSetup(value = "classpath:dataset.xml", type = DatabaseOperation.CLEAN_INSERT)
    @DatabaseTearDown(value = "classpath:dataset.xml", type = DatabaseOperation.DELETE_ALL)
    public void testFindDomainById() {
        try {
            assertEquals(TAG_ID_1, tagDAO.findDomainById(TAG_ID_1).getTagId());
        } catch (DAOException e) {
            LOG.error(e);
        }
    }

    @Test
    @DatabaseSetup(value = "classpath:dataset.xml", type = DatabaseOperation.CLEAN_INSERT)
    @DatabaseTearDown(value = "classpath:dataset.xml", type = DatabaseOperation.DELETE_ALL)
    public void testDelete() {

        try {
            tagDAO.delete(TAG_ID_1);
            assertEquals(null, tagDAO.findDomainById(TAG_ID_1));
        } catch (DAOException e) {
            LOG.error(e);
        }
    }

    @Test
    public void testCreate() {
        try {
            TAG.setTagId(tagDAO.create(TAG));
            assertEquals(TAG,tagDAO.findDomainById(TAG.getTagId()));
        } catch (DAOException e) {
            LOG.error(e);
        }

    }

    @Test
    @DatabaseSetup(value = "classpath:dataset.xml", type = DatabaseOperation.CLEAN_INSERT)
    @DatabaseTearDown(value = "classpath:dataset.xml", type = DatabaseOperation.DELETE_ALL)
    public void testUpdate(){
        Tag tag = new Tag(TAG_ID_1,NEW_TAG_NAME);
        try {
            tagDAO.update(tag);
            assertEquals(tag.toString().trim(),tagDAO.findDomainById(TAG_ID_1).toString().trim());
        } catch (DAOException e) {
            LOG.error(e);
        }
    }
}
