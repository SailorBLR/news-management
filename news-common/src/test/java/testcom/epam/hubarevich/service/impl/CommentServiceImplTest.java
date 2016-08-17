package testcom.epam.hubarevich.service.impl;

import com.epam.hubarevich.dao.CommentDAO;
import com.epam.hubarevich.dao.exception.DAOException;
import com.epam.hubarevich.domain.Comment;
import com.epam.hubarevich.service.exception.LogicException;
import com.epam.hubarevich.service.impl.CommentServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Calendar;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

/**
 * Created by Anton_Hubarevich on 6/27/2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class CommentServiceImplTest {
    private final Logger LOG = LogManager.getLogger(CommentServiceImplTest.class);

    @InjectMocks
    private CommentServiceImpl commentService;

    @Mock
    CommentDAO commentDAO;

    @Test
    public void testCreateAuthor() {
        try {
            Comment comment = new Comment(1L,1L, "Name","Anton", Calendar.getInstance().getTime());
            when(commentDAO.create(comment)).thenReturn(1L);
            Assert.assertEquals(Long.valueOf(1L), commentService.addComment(comment));
        } catch (DAOException | LogicException e) {
            LOG.error(e);
        }
    }

    @Test (expected = NumberFormatException.class)
    public void testDeleteComment() {
        try {
            doThrow(new NumberFormatException()).when(commentDAO).delete(anyLong());
            commentService.deleteComment(1L);
        } catch (DAOException | LogicException e) {
            LOG.error(e);
        }
    }

    @Test (expected = NumberFormatException.class)
    public void testUpdateComment() {
        try {
            Comment comment = new Comment(1L,1L, "Name","Anton", Calendar.getInstance().getTime());
            doThrow(new NumberFormatException()).when(commentDAO).update(comment);
            commentService.updateComment(comment);
        } catch (DAOException | LogicException e) {
            LOG.error(e);
        }
    }
}
