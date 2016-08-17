package testcom.epam.hubarevich.service.impl;

import com.epam.hubarevich.dao.AuthorDAO;
import com.epam.hubarevich.dao.exception.DAOException;
import com.epam.hubarevich.domain.Author;
import com.epam.hubarevich.service.exception.LogicException;
import com.epam.hubarevich.service.impl.AuthorServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.LinkedList;
import java.util.List;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

/**
 * Created by Anton_Hubarevich on 6/24/2016.
 */
@RunWith(MockitoJUnitRunner.class)

public class AuthorServiceImpTest {
    private final Logger LOG = LogManager.getLogger(AuthorServiceImpTest.class);

    @InjectMocks
    private AuthorServiceImpl authorService;

    @Mock
    AuthorDAO authorDAO;

    @Test
    public void testCreateAuthor() {
        try {
            Author author = new Author(1L, "Name");
            when(authorDAO.create(author)).thenReturn(1L);
            Assert.assertEquals(Long.valueOf(1L), authorService.createAuthor(author));
        } catch (DAOException | LogicException e) {
            LOG.error(e);
        }
    }

    @Test (expected = NumberFormatException.class)
    public void testDeleteAuthor() {
        try {
            doThrow(new NumberFormatException()).when(authorDAO).delete(anyLong());
            authorService.deleteAuthor(anyLong());
        } catch (DAOException | LogicException e) {
            LOG.error(e);
        }
    }

    @Test (expected = NumberFormatException.class)
    public void testUpdateAuthor() {
        try {
            Author author = new Author(1L, "Name");
            doThrow(new NumberFormatException()).when(authorDAO).update(author);
            authorService.updateAuthor(author);
        } catch (DAOException | LogicException e) {
            LOG.error(e);
        }
    }

    @Test
    public void testGetAuthorByNewsId() {
        try {
            Author author = new Author(1L, "Name");
            when(authorDAO.findAuthorByNewsId(1L)).thenReturn(author);
            Assert.assertEquals(author, authorService.getAuthorByNewsId(1L));
        } catch (DAOException | LogicException e) {
            LOG.error(e);
        }
    }

    @Test
    public void testGetListOfAuthors() {
        try {
            List<Author> authors = new LinkedList<>();
            Author author = new Author(1L, "Name");
            Author author2 = new Author(2L, "Name2");
            Author author3 = new Author(3L, "Name3");
            authors.add(author);
            authors.add(author2);
            authors.add(author3);
            when(authorDAO.findAll()).thenReturn(authors);
            Assert.assertEquals(authors, authorService.getListOfAuthors());
        } catch (DAOException | LogicException e) {
            LOG.error(e);
        }
    }
}
