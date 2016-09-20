package testcom.epam.hubarevich.service.impl;

import com.epam.hubarevich.dao.AuthorDAO;
import com.epam.hubarevich.dao.CommentDAO;
import com.epam.hubarevich.dao.NewsDAO;
import com.epam.hubarevich.dao.TagDAO;
import com.epam.hubarevich.dao.exception.DAOException;
import com.epam.hubarevich.domain.Author;
import com.epam.hubarevich.domain.News;
import com.epam.hubarevich.domain.Tag;
import com.epam.hubarevich.domain.dto.NewsDTO;
import com.epam.hubarevich.domain.dto.SearchDTO;
import com.epam.hubarevich.service.exception.LogicException;
import com.epam.hubarevich.service.impl.NewsServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

/**
 * Created by Anton_Hubarevich on 6/24/2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class NewsServiceImplTest {
    private final Logger LOG = LogManager.getLogger(NewsServiceImplTest.class);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String creation = "1988-12-11 08:45:33";
    String modification = "1989-12-11 00:0:00";

    @InjectMocks
    private NewsServiceImpl newsService;

    @Mock
    private NewsDAO newsDAO;
    @Mock
    private AuthorDAO authorDAO;
    @Mock
    private TagDAO tagDAO;
    @Mock
    private CommentDAO commentDAO;

    @Test
    public void testCreateNews() {
        try {
            News news = new News(1L, "f", "f", "gg", sdf.parse(creation),
                    sdf.parse(modification));
            NewsDTO newsDTO = new NewsDTO();
            Tag tag = new Tag(1L, "Belarus");
            List<Tag> tags = new LinkedList<>();
            Author author = new Author(1L, "Name");
            tags.add(tag);
            newsDTO.setNews(news);
            newsDTO.setTags(tags);
            newsDTO.setAuthor(author);
            when(newsDAO.create(newsDTO.getNews())).thenReturn(1L);
            Assert.assertEquals(Long.valueOf(1L), newsService.createNews(newsDTO));
        } catch (DAOException|ParseException|LogicException e){
            LOG.error(e);
        }


    }

    @Test(expected = NumberFormatException.class)
    public void testDeleteNews() {
        try {
            doThrow(new NumberFormatException()).when(newsDAO).delete(anyLong());
            newsService.deleteNews(anyLong());
        } catch (DAOException|LogicException e){
            LOG.error(e);
        }
    }

    @Test(expected = NumberFormatException.class)
    public void testUpdateNews() {
        try {
            News news = new News(1L, "f", "f", "gg", sdf.parse(creation),
                    sdf.parse(modification));
            NewsDTO newsDTO = new NewsDTO();
            Tag tag = new Tag(1L, "Belarus");
            List<Tag> tags = new LinkedList<>();
            Author author = new Author(1L, "Name");
            tags.add(tag);
            newsDTO.setNews(news);
            newsDTO.setTags(tags);
            newsDTO.setAuthor(author);
            doThrow(new NumberFormatException()).when(newsDAO).update(news);
            newsService.updateNews(newsDTO);
        } catch (DAOException|LogicException|ParseException e){
            LOG.error(e);
        }
    }



    @Test
    public void testGetSearchNewsQuantity() {

        SearchDTO searchDTO = new SearchDTO();
        try {
            when(newsDAO.getTotalNewsQuantity(searchDTO)).thenReturn(1);
            assertEquals(1,newsService.getSearchNewsQuantity(searchDTO));
        } catch (DAOException|LogicException e) {
            LOG.error(e);
        }
    }


    @Test
    public void testGetNewsBySearchCriteria() {
        SearchDTO searchDTO = new SearchDTO();
        List<News> news = new ArrayList<>();
        List<Tag> tags = new ArrayList<>();
        Author author = new Author();
        try {
            when(newsDAO.getTotalNewsQuantity(searchDTO)).thenReturn(20);
            when(newsDAO.getPaginatedListBySearchCriteria(searchDTO,1,5)).thenReturn(news);
            when(tagDAO.getTagsByNewsId(1L)).thenReturn(tags);
            when(authorDAO.findAuthorByNewsId(1L)).thenReturn(author);


        } catch (DAOException e) {
            LOG.error(e);
        }
    }
}
