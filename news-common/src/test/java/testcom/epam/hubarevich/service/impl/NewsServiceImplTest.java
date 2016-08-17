package testcom.epam.hubarevich.service.impl;

import com.epam.hubarevich.dao.AuthorDAO;
import com.epam.hubarevich.dao.NewsDAO;
import com.epam.hubarevich.dao.TagDAO;
import com.epam.hubarevich.domain.Author;
import com.epam.hubarevich.domain.News;
import com.epam.hubarevich.domain.Tag;
import com.epam.hubarevich.domain.dto.NewsDTO;
import com.epam.hubarevich.domain.dto.SearchDTO;
import com.epam.hubarevich.service.NewsService;
import com.epam.hubarevich.service.impl.NewsServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

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

    @Test
    public void testCreateNews() throws Exception {


        News news = new News(1L,"f","f","gg",sdf.parse(creation),
                sdf.parse(modification));
        NewsDTO newsDTO = new NewsDTO();
        Tag tag = new Tag(1L,"Belarus");
        List<Tag> tags = new LinkedList<>();
        Author author = new Author(1L,"Name");
        tags.add(tag);
        newsDTO.setNews(news);
        newsDTO.setTags(tags);
        newsDTO.setAuthor(author);
        when(newsDAO.create(newsDTO.getNews())).thenReturn(1L);
        Assert.assertEquals(Long.valueOf(1L), newsService.createNews(newsDTO));
    }

    @Test(expected = NumberFormatException.class)
    public void testDeleteNews() throws Exception {
        doThrow(new NumberFormatException()).when(newsDAO).delete(anyLong());
        newsService.deleteNews(anyLong());
    }

    @Test (expected = NumberFormatException.class)
    public void testUpdate() throws Exception {
        News news = new News(1L,"f","f","gg",sdf.parse(creation),
                sdf.parse(modification));
        NewsDTO newsDTO = new NewsDTO();
        Tag tag = new Tag(1L,"Belarus");
        List<Tag> tags = new LinkedList<>();
        Author author = new Author(1L,"Name");
        tags.add(tag);
        newsDTO.setNews(news);
        newsDTO.setTags(tags);
        newsDTO.setAuthor(author);
        doThrow(new NumberFormatException()).when(newsDAO).update(news);
        newsService.updateNews(newsDTO);
    }

    @Test
    public void testSearchForNewsAuthor() throws Exception {
        Author author = new Author();
        Tag tag = new Tag(1L,"Belarus");
        SearchDTO searchDTO = new SearchDTO();
        List<News> newses = new LinkedList<>();
        List<Tag> tags = new LinkedList<>();
        News news = new News(1L,"f","f","gg",sdf.parse(creation),
                sdf.parse(modification));
        tags.add(tag);
        author.setAuthorId(1L);
        newses.add(news);
        searchDTO.setAuthor(author);
        when(newsDAO.findNewsByAuthor(author)).thenReturn(newses);
        when(authorDAO.findAuthorByNewsId(anyLong())).thenReturn(author);
        when(tagDAO.getTagsByNewsId(anyLong())).thenReturn(tags);
        Assert.assertEquals(newses.toString().substring(1,newses.toString().length()-1),
                newsService.searchForNews(searchDTO).iterator().next().getNews().toString());
    }

    @Test
    public void testSearchForNewsTags() throws Exception {
        Author author = new Author();
        Tag tag = new Tag(1L,"Belarus");
        SearchDTO searchDTO = new SearchDTO();
        List<News> newses = new LinkedList<>();
        List<Tag> tags = new LinkedList<>();
        News news = new News(1L,"f","f","gg",sdf.parse(creation),
                sdf.parse(modification));
        tags.add(tag);
        author.setAuthorId(1L);
        newses.add(news);
        searchDTO.setTags(tags);
        when(newsDAO.findNewsByTags(tags)).thenReturn(newses);
        when(authorDAO.findAuthorByNewsId(anyLong())).thenReturn(author);
        when(tagDAO.getTagsByNewsId(anyLong())).thenReturn(tags);
        Assert.assertEquals(newses.toString().substring(1,newses.toString().length()-1),
                newsService.searchForNews(searchDTO).iterator().next().getNews().toString());
    }


    @Test
    public void testSearchForNewsCommented() throws Exception {

        List<News> newses = new LinkedList<>();
        News news = new News(1L,"f","f","gg",sdf.parse(creation),
                sdf.parse(modification));
        newses.add(news);
        when(newsDAO.getMostCommentedNews(2)).thenReturn(newses);
        when(newsDAO.findAll()).thenReturn(newses);
        assertTrue((newsService.searchForNewsCommented(true,2)).iterator().next().getNews().equals(news));

    }

}
