package com.epam.hubarevich.dao;

/**
 * Created by Anton_Hubarevich on 8/31/2016.
 */

import com.epam.hubarevich.dao.exception.DAOException;
import com.epam.hubarevich.dao.impl.AuthorDaoImpl;
import com.epam.hubarevich.dao.impl.NewsDaoImpl;
import com.epam.hubarevich.domain.Author;
import com.epam.hubarevich.domain.News;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:beans-test.xml")
@Transactional
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DbUnitTestExecutionListener.class,
        TransactionalTestExecutionListener.class})
public class NewsDaoImplTest {
    private final Logger LOG = LogManager.getLogger(NewsDaoImplTest.class);
    private final int NEWS_QUANTITY = 3;
    private final int TAGS_QUANTITY = 3;
    private final Long N_ID_1 = 1L;
    private final Long N_ID_2 = 2L;
    private final Long N_ID_3 = 3L;
    private final Long A_ID_2 = 2L;
    private final String NAME_1 = "Ivan Ivanov";
    private final String TITLE_1 = "Test";
    private final String SHORT_1 = "Short";
    private final String FULL_1 = "Full";
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private final String CREATION_TO_PARSE = "1999-05-06";
    private final String TITLE_3 = "Test3";
    private final Long ID_1 = 1L;
    private final Author AUTHOR = new Author(ID_1, NAME_1);


    @Autowired
    AuthorDAO authorDao;
    @Autowired
    NewsDAO newsDao;

    @Autowired
    CommentDAO commentDAO;

    @Autowired
    TagDAO tagDAO;

    @Test
    @DatabaseSetup(value = "classpath:dataset.xml", type = DatabaseOperation.CLEAN_INSERT)
    @DatabaseTearDown(value = "classpath:dataset.xml", type = DatabaseOperation.DELETE_ALL)
    public void testAddNewsAuthor() {
        try {
            newsDao.addNewsAuthor(N_ID_2, A_ID_2);
        } catch (DAOException e) {
            LOG.error(e);
        }
    }

    @Test
    public void testGetMostCommentedNews() throws Exception {

    }

    @Test
    @DatabaseSetup(value = "classpath:dataset.xml", type = DatabaseOperation.CLEAN_INSERT)
    @DatabaseTearDown(value = "classpath:dataset.xml", type = DatabaseOperation.DELETE_ALL)
    public void testAddTagsNews() {
        try {

            News news = new News();
            news.setTitle(TITLE_3);
            newsDao.addTagsNews(newsDao.getNewsByNewsTitle(news).getNewsId(),tagDAO.findAll());
            System.out.println();
            assertTrue(TAGS_QUANTITY==tagDAO.getTagsByNewsId(newsDao.getNewsByNewsTitle(news).getNewsId()).size());
        } catch (DAOException e) {
            LOG.error(e);
        }
    }

    @Test
    @DatabaseSetup(value = "classpath:dataset.xml", type = DatabaseOperation.CLEAN_INSERT)
    @DatabaseTearDown(value = "classpath:dataset.xml", type = DatabaseOperation.DELETE_ALL)
    public void testFindNewsByAuthor() {

        try {
            List<News> news = new ArrayList<>();
            news.add(newsDao.findDomainById(N_ID_1));
            news.add(newsDao.findDomainById(N_ID_3));
            assertTrue(newsDao.findNewsByAuthor(AUTHOR).containsAll(news));
        } catch (DAOException e) {
            LOG.error(e);
        }

    }

    @Test
    public void testFindNewsByTags() throws Exception {

    }

    @Test
    public void testGetTotalNewsQuantity() throws Exception {

    }

    @Test
    public void testGetPaginatedListBySearchCriteria() {

    }

    @Test
    public void testGetPrevNextIds() throws Exception {

    }

    @Test
    @DatabaseSetup(value = "classpath:dataset.xml", type = DatabaseOperation.CLEAN_INSERT)
    @DatabaseTearDown(value = "classpath:dataset.xml", type = DatabaseOperation.DELETE_ALL)
    public void testGetNewsByNewsTitle() {
        News news = new News();
        news.setTitle(TITLE_1);
        try {
            assertEquals(news.getTitle(), newsDao.getNewsByNewsTitle(news).getTitle());
        } catch (DAOException e) {
            LOG.error(e);
        }


    }

    @Test
    @DatabaseSetup(value = "classpath:dataset.xml", type = DatabaseOperation.CLEAN_INSERT)
    @DatabaseTearDown(value = "classpath:dataset.xml", type = DatabaseOperation.DELETE_ALL)
    public void testFindAll() {
        try {
            List<News> news = newsDao.findAll();
            assertTrue(NEWS_QUANTITY == news.size());
        } catch (DAOException e) {
            LOG.error(e);
        }

    }

    @Test
    @DatabaseSetup(value = "classpath:dataset.xml", type = DatabaseOperation.CLEAN_INSERT)
    @DatabaseTearDown(value = "classpath:dataset.xml", type = DatabaseOperation.DELETE_ALL)
    public void testFindDomainById() {
        try {
            assertEquals(newsDao.findDomainById(N_ID_1).getTitle(), TITLE_1);
        } catch (DAOException e) {
            LOG.error(e);
        }
    }

    @Test
    @DatabaseSetup(value = "classpath:dataset.xml", type = DatabaseOperation.CLEAN_INSERT)
    @DatabaseTearDown(value = "classpath:dataset.xml", type = DatabaseOperation.DELETE_ALL)
    public void testDelete() {
        try {
            newsDao.delete(N_ID_1);
            assertEquals(null, newsDao.findDomainById(N_ID_1));
        } catch (DAOException e) {
            LOG.error(e);
        }
    }

    @Test
    public void testCreate() {

        try {
            News news = new News(N_ID_1, TITLE_1, SHORT_1, FULL_1, sdf.parse(CREATION_TO_PARSE), sdf.parse(CREATION_TO_PARSE));
            news.setNewsId(newsDao.create(news));
            assertEquals(news, newsDao.findDomainById(news.getNewsId()));
        } catch (ParseException e) {
            LOG.error(e);
        } catch (DAOException e) {
            LOG.error(e);
        }

    }

    @Test
    public void testUpdate() throws Exception {
        try {
            News news = new News(N_ID_1, TITLE_1, SHORT_1, FULL_1, sdf.parse(CREATION_TO_PARSE), sdf.parse(CREATION_TO_PARSE));
            news.setNewsId(newsDao.create(news));

            news.setTitle(TITLE_3);
            news.setNewsModificationDate(Calendar.getInstance().getTime());
            newsDao.update(news);
            assertEquals(news.getTitle(), newsDao.findDomainById(news.getNewsId()).getTitle());
            assertTrue(newsDao.findDomainById(news.getNewsId()).getNewsCreationDate().getTime() <
                    newsDao.findDomainById(news.getNewsId()).getNewsModificationDate().getTime());
        } catch (ParseException e) {
            LOG.error(e);
        } catch (DAOException e) {
            LOG.error(e);
        }
    }
}