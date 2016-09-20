package com.epam.hubarevich.dao;

/**
 * Created by Anton_Hubarevich on 8/31/2016.
 */

import com.epam.hubarevich.dao.exception.DAOException;
import com.epam.hubarevich.domain.Author;
import com.epam.hubarevich.domain.News;
import com.epam.hubarevich.domain.Tag;
import com.epam.hubarevich.domain.dto.SearchDTO;
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
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
        "classpath:beans-test-hibernate.xml",
        "classpath:beans-test-eclipselink.xml"})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DbUnitTestExecutionListener.class,
        TransactionalTestExecutionListener.class})
@ActiveProfiles("elink")
@Transactional
public class NewsDaoImplTest {
    private final Logger LOG = LogManager.getLogger(NewsDaoImplTest.class);
    private final int NEWS_QUANTITY = 4;
    private final int TAGS_QUANTITY = 3;
    private final Long ID_1 = 1L;
    private final Long ID_2 = 2L;
    private final Long ID_3 = 3L;
    private final Long ID_4 = 4L;
    private final String NAME_1 = "Ivan Ivanov";
    private final String TITLE_1 = "Test";
    private final String SHORT_1 = "Short";
    private final String FULL_1 = "Full";
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private final String CREATION_TO_PARSE = "1999-05-06";
    private final String TITLE_3 = "Test3";
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
    public void testAddNewsAuthor() throws DAOException {
        newsDao.addNewsAuthor(ID_2, ID_2);
        assertEquals(ID_2, newsDao.findNewsByAuthor(new Author(ID_2, "Author")).iterator().next().getNewsId());
    }


    @Test
    @DatabaseSetup(value = "classpath:dataset.xml", type = DatabaseOperation.CLEAN_INSERT)
    @DatabaseTearDown(value = "classpath:dataset.xml", type = DatabaseOperation.DELETE_ALL)
    public void testAddTagsNews() throws DAOException {

        News news = new News();
        news.setTitle(TITLE_3);
        newsDao.addTagsNews(newsDao.getNewsByNewsTitle(news).getNewsId(), tagDAO.findAll());
        assertTrue(TAGS_QUANTITY == tagDAO.getTagsByNewsId(newsDao.getNewsByNewsTitle(news).getNewsId()).size());

    }

    @Test
    @DatabaseSetup(value = "classpath:dataset.xml", type = DatabaseOperation.CLEAN_INSERT)
    @DatabaseTearDown(value = "classpath:dataset.xml", type = DatabaseOperation.DELETE_ALL)
    public void testFindNewsByAuthor() throws DAOException {

        List<News> news = new ArrayList<>();
        news.add(newsDao.findDomainById(ID_1));
        news.add(newsDao.findDomainById(ID_3));
        assertTrue(newsDao.findNewsByAuthor(AUTHOR).containsAll(news));
    }

    @Test
    @DatabaseSetup(value = "classpath:dataset.xml", type = DatabaseOperation.CLEAN_INSERT)
    @DatabaseTearDown(value = "classpath:dataset.xml", type = DatabaseOperation.DELETE_ALL)
    public void testFindNewsByTags() throws DAOException {

        List<Tag> tags = tagDAO.findAll();
        assertTrue(4 == newsDao.findNewsByTags(tags).size());
    }

    @Test
    @DatabaseSetup(value = "classpath:dataset.xml", type = DatabaseOperation.CLEAN_INSERT)
    @DatabaseTearDown(value = "classpath:dataset.xml", type = DatabaseOperation.DELETE_ALL)
    public void testGetTotalNewsQuantity() throws DAOException {

        SearchDTO searchDTO = new SearchDTO();
        List<Tag> tags = new ArrayList<>();
        tags.addAll(tagDAO.getTagsByNewsId(ID_2));
        tags.add(tagDAO.findDomainById(ID_2));
        searchDTO.setTags(tags);
        searchDTO.setAuthor(AUTHOR);
        searchDTO.setNextId(2L);
        assertEquals(4, newsDao.getTotalNewsQuantity(searchDTO));

    }

    @Test
    @DatabaseSetup(value = "classpath:dataset.xml", type = DatabaseOperation.CLEAN_INSERT)
    @DatabaseTearDown(value = "classpath:dataset.xml", type = DatabaseOperation.DELETE_ALL)
    public void testGetPaginatedListBySearchCriteria() throws DAOException {

        SearchDTO searchDTO = new SearchDTO();

        List<Tag> tags = new ArrayList<>();
        tags.add(tagDAO.findDomainById(ID_3));
        searchDTO.setTags(tags);
        searchDTO.setAuthor(AUTHOR);
        assertEquals(3, newsDao.getPaginatedListBySearchCriteria(searchDTO, 1, 5).size());
    }

    @Test
    @DatabaseSetup(value = "classpath:dataset.xml", type = DatabaseOperation.CLEAN_INSERT)
    @DatabaseTearDown(value = "classpath:dataset.xml", type = DatabaseOperation.DELETE_ALL)
    public void testGetPrevNextIds() throws DAOException {
        SearchDTO searchDTO = new SearchDTO();
        List<Tag> tags = new ArrayList<>();
        tags.addAll(tagDAO.getTagsByNewsId(ID_2));
        tags.add(tagDAO.findDomainById(ID_2));
        searchDTO.setTags(tags);
        searchDTO.setAuthor(AUTHOR);
        newsDao.getPrevNextIds(searchDTO, ID_1);

        assertTrue(ID_3.equals(searchDTO.getPrevId()));
        assertTrue(ID_4.equals(searchDTO.getNextId()));
    }

    @Test
    @DatabaseSetup(value = "classpath:dataset.xml", type = DatabaseOperation.CLEAN_INSERT)
    @DatabaseTearDown(value = "classpath:dataset.xml", type = DatabaseOperation.DELETE_ALL)
    public void testGetNewsByNewsTitle() throws DAOException {
        News news = new News();
        news.setTitle(TITLE_1);
        News news_null = new News();
        news_null.setTitle("T");

        assertEquals(news.getTitle(), newsDao.getNewsByNewsTitle(news).getTitle());
        assertEquals(null, newsDao.getNewsByNewsTitle(news_null));


    }

    @Test
    @DatabaseSetup(value = "classpath:dataset.xml", type = DatabaseOperation.CLEAN_INSERT)
    @DatabaseTearDown(value = "classpath:dataset.xml", type = DatabaseOperation.DELETE_ALL)
    public void testFindAll() throws DAOException {

        List<News> news = newsDao.findAll();
        assertTrue(NEWS_QUANTITY == news.size());
    }

    @Test
    @DatabaseSetup(value = "classpath:dataset.xml", type = DatabaseOperation.CLEAN_INSERT)
    @DatabaseTearDown(value = "classpath:dataset.xml", type = DatabaseOperation.DELETE_ALL)
    public void testFindDomainById() throws DAOException {

        assertEquals(newsDao.findDomainById(ID_1).getTitle(), TITLE_1);
    }

   /* @Test
    @DatabaseSetup(value = "classpath:dataset.xml", type = DatabaseOperation.CLEAN_INSERT)
    @DatabaseTearDown(value = "classpath:dataset.xml", type = DatabaseOperation.DELETE_ALL)
    public void testDelete() throws DAOException {

        newsDao.delete(N_ID_1);
        assertEquals(null, newsDao.findDomainById(N_ID_1));
    }*/

    @Test
    public void testCreate() throws ParseException, DAOException {

        News news = new News(ID_1, TITLE_1, SHORT_1, FULL_1, sdf.parse(CREATION_TO_PARSE), sdf.parse(CREATION_TO_PARSE));
        news.setNewsId(newsDao.create(news));
        assertEquals(news, newsDao.findDomainById(news.getNewsId()));

    }

    @Test
    public void testUpdate() throws DAOException, ParseException {
        News news = new News(ID_1, TITLE_1, SHORT_1, FULL_1, sdf.parse(CREATION_TO_PARSE), sdf.parse(CREATION_TO_PARSE));
        news.setNewsId(newsDao.create(news));

        news.setTitle(TITLE_3);
        news.setNewsModificationDate(Calendar.getInstance().getTime());
        newsDao.update(news);
        assertEquals(news.getTitle(), newsDao.findDomainById(news.getNewsId()).getTitle());
        assertTrue(newsDao.findDomainById(news.getNewsId()).getNewsCreationDate().getTime() <
                newsDao.findDomainById(news.getNewsId()).getNewsModificationDate().getTime());
    }
}
