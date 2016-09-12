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
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
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
@ContextConfiguration({
        "classpath:beans-test-hibernate.xml",
        "classpath:beans-test-eclipselink.xml"})
@Transactional
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DbUnitTestExecutionListener.class,
        TransactionalTestExecutionListener.class})

@ActiveProfiles("elink")
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
    public void testGetTagsByNewsId() throws DAOException {
        List<Tag> tags = new ArrayList<>();
        tags.add(tagDAO.findDomainById(TAG_ID_1));
        assertTrue(tags.containsAll(tagDAO.getTagsByNewsId(NEWS_ID)));


    }

    @Test
    @DatabaseSetup(value = "classpath:dataset.xml", type = DatabaseOperation.CLEAN_INSERT)
    @DatabaseTearDown(value = "classpath:dataset.xml", type = DatabaseOperation.DELETE_ALL)
    public void testFindAll() throws DAOException {
        assertEquals(TAGS_SIZE, tagDAO.findAll().size());

    }

    @Test
    @DatabaseSetup(value = "classpath:dataset.xml", type = DatabaseOperation.CLEAN_INSERT)
    @DatabaseTearDown(value = "classpath:dataset.xml", type = DatabaseOperation.DELETE_ALL)
    public void testFindDomainById() throws DAOException {
        assertEquals(TAG_ID_1, tagDAO.findDomainById(TAG_ID_1).getTagId());

    }

   /* @Test
    @DatabaseSetup(value = "classpath:dataset.xml", type = DatabaseOperation.CLEAN_INSERT)
    @DatabaseTearDown(value = "classpath:dataset.xml", type = DatabaseOperation.DELETE_ALL)
    public void testDelete() {

        try {
            tagDAO.delete(TAG_ID_1);
            assertEquals(null, tagDAO.findDomainById(TAG_ID_1));
        } catch (DAOException e) {
            LOG.error(e);
        }
    }*/

    @Test
    public void testCreate() throws DAOException {
        TAG.setTagId(tagDAO.create(TAG));
        assertEquals(TAG, tagDAO.findDomainById(TAG.getTagId()));
    }

    @Test
    @DatabaseSetup(value = "classpath:dataset.xml", type = DatabaseOperation.CLEAN_INSERT)
    @DatabaseTearDown(value = "classpath:dataset.xml", type = DatabaseOperation.DELETE_ALL)
    public void testUpdate() throws DAOException {

        Tag tag = new Tag(TAG_ID_1, NEW_TAG_NAME);
        tagDAO.update(tag);
        assertEquals(tag.toString().trim(), tagDAO.findDomainById(TAG_ID_1).toString().trim());
    }
}
