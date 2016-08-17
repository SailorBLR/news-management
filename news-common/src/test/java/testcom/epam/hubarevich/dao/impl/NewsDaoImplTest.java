package testcom.epam.hubarevich.dao.impl;

import com.epam.hubarevich.dao.exception.DAOException;
import com.epam.hubarevich.dao.impl.AuthorDAOImpl;
import com.epam.hubarevich.dao.impl.NewsDAOImpl;
import com.epam.hubarevich.dao.impl.TagDAOImpl;
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
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import java.sql.Date;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Anton_Hubarevich on 6/21/2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:beans-test.xml")
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DbUnitTestExecutionListener.class,
        TransactionalTestExecutionListener.class})
@DatabaseSetup(value = "classpath:dataset.xml", type = DatabaseOperation.CLEAN_INSERT)
@DatabaseTearDown(value = "classpath:dataset.xml", type = DatabaseOperation.DELETE_ALL)

public class NewsDaoImplTest {

    private final Logger LOG = LogManager.getLogger(NewsDaoImplTest.class);

    @Autowired
    NewsDAOImpl newsDAO = new NewsDAOImpl();
    @Autowired
    AuthorDAOImpl authorDAO = new AuthorDAOImpl();
    @Autowired
    TagDAOImpl tagDAO = new TagDAOImpl();


    @Test
    public void testCreate() {
        try {
            Date date = new Date(Calendar.getInstance().getTime().getTime());
            News news = new News(1L, "Test", "STExt", "FText", Calendar.getInstance().getTime(),
                    Calendar.getInstance().getTime());
            Assert.assertNotEquals(Long.valueOf(0L), newsDAO.create(news));
        } catch (DAOException e) {
            LOG.error(e);
        }
    }


    @Test
    public void testDelete() {
        try {
            newsDAO.delete(1L);
            assertEquals(newsDAO.findDomainById(1L), null);
        } catch (DAOException e) {
            LOG.error(e);
        }
    }


    @Test
    public void testUpdate() {
        News news;
        try {
            news = new News(1L, "Test", "STExt", "FText", newsDAO.findDomainById(1L).getNewsCreationDate(),
                    Calendar.getInstance().getTime());
            newsDAO.update(news);
            assertEquals(newsDAO.findDomainById(1L).toString().trim(), news.toString().trim());
        } catch (DAOException e) {
            LOG.error(e);
        }
    }


    @Test
    public void testFindDomainById() {
        try {
            News news = new News();
            news.setNewsId(1L);
            assertEquals(newsDAO.findDomainById(1L).getNewsId(), news.getNewsId());
        } catch (DAOException e) {
            LOG.error(e);
        }
    }


    @Test
    public void testFindAll() {
        try {
            List<News> newses = new LinkedList<>();
            newses.add(newsDAO.findDomainById(1L));
            newses.add(newsDAO.findDomainById(2L));
            newses.add(newsDAO.findDomainById(3L));
            assertTrue(newses.containsAll(newsDAO.findAll()));
        } catch (DAOException e) {
            LOG.error(e);
        }
    }

    @Test
    public void testAddNewsAuthor() {
        try {
            Author author = new Author(1L, "Ivan Ivanov");
            newsDAO.addNewsAuthor(1L, 1L);
            assertEquals(authorDAO.findAuthorByNewsId(1L).toString().trim(), author.toString().trim());
        } catch (DAOException e) {
            LOG.error(e);
        }
    }


    @Test
    public void testGetMostCommentedNews() {

        try {
            List<News> newses = new LinkedList<>();
            newses.add(newsDAO.findDomainById(1L));
            newses.add(newsDAO.findDomainById(2L));
            assertTrue(newsDAO.getMostCommentedNews(2).equals(newses));
        } catch (DAOException e) {
            LOG.error(e);
        }
    }

    @Test
    public void testAddTagsNews() {
        try {
            List<Tag> tags = new LinkedList<>();
            Tag tag = new Tag(1L, "Religion");
            tags.add(tag);
            tag = new Tag(2L, "Belarus");
            tags.add(tag);
            newsDAO.addTagsNews(1L, tags);
            assertTrue(tagDAO.getTagsByNewsId(1L).containsAll(tags));
        } catch (DAOException e) {
            LOG.error(e);
        }
    }


    @Test
    public void testFindNewsByTags() {
        try {
            List<News> newses = new LinkedList<>();
            List<Tag> tags = new LinkedList<>();
            Tag tag = new Tag(1L, "Religion");
            tags.add(tag);
            tag = new Tag(2L, "Belarus");
            tags.add(tag);
            newses.add(newsDAO.findDomainById(1L));
            assertTrue(newsDAO.findNewsByTags(tags).equals(newses));
        } catch (DAOException e) {
            LOG.error(e);
        }
    }

    @Test
    public void testFindNewsByAuthor() {
        try {
            List<News> newses = new LinkedList<>();
            Author author = new Author(1L, "Ivan Ivanov");
            newses.add(newsDAO.findDomainById(1L));
            newses.add(newsDAO.findDomainById(3L));
            assertTrue(newsDAO.findNewsByAuthor(author).containsAll(newses));
        } catch (DAOException e) {
            LOG.error(e);
        }
    }

    @Test
    public void testGetTotalNewsQuantity() {
        int result;
        try {
            result = newsDAO.getTotalNewsQuantity(new SearchDTO());
            assertTrue(result == 3);
        } catch (DAOException e) {
            LOG.error(e);
        }
    }

    @Test
    public void testGetPaginatedListBySearchCriteria() {
        SearchDTO searchDTO = new SearchDTO();
        searchDTO.setAuthor(new Author(1L, "Anton"));
        Tag tag = new Tag(1L, "Belarus");
        List<Tag> tags = new LinkedList<>();
        tags.add(tag);
        searchDTO.setTags(tags);
        News news = new News();
        news.setNewsId(1L);
        try {
            assertTrue(news.getNewsId() ==
                    newsDAO.getPaginatedListBySearchCriteria(searchDTO, 0, 2).iterator().next().getNewsId());
        } catch (DAOException e) {
            LOG.error(e);
        }

    }

    @Test
    public void testGetPrevNextIds() {
        SearchDTO searchDTO = new SearchDTO();
        searchDTO.setAuthor(new Author(1L, "Anton"));
        Tag tag = new Tag(1L, "Belarus");
        List<Tag> tags = new LinkedList<>();
        tags.add(tag);
        searchDTO.setTags(tags);
        News news = new News();
        news.setNewsId(1L);
        try {
            newsDAO.getPrevNextIds(searchDTO, news.getNewsId());
        } catch (DAOException e) {
            LOG.error(e);
        }
    }

    @Test
    public void testGetNewsByNewsTitle(){
        News news = new News(1L, "Test", "STExt", "FText", Calendar.getInstance().getTime(),
                Calendar.getInstance().getTime());
        try {
            newsDAO.create(news);
            assertTrue(newsDAO.getNewsByNewsTitle(news).getTitle().equals(news.getTitle()));
        } catch (DAOException e){
            LOG.error(e);
        }

    }
}
