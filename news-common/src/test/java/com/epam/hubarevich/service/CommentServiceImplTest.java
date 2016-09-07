package com.epam.hubarevich.service;

import com.epam.hubarevich.dao.CommentDAO;
import com.epam.hubarevich.dao.NewsDAO;
import com.epam.hubarevich.dao.exception.DAOException;
import com.epam.hubarevich.domain.Comment;
import com.epam.hubarevich.domain.News;
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
 * Test class for Comment service
 */
@RunWith(MockitoJUnitRunner.class)
public class CommentServiceImplTest {
    private final Logger LOG = LogManager.getLogger(CommentServiceImplTest.class);

    @InjectMocks
    private CommentServiceImpl commentService;

    @Mock
    CommentDAO commentDAO;

    @Mock
    NewsDAO newsDAO;

    @Test
    public void testAddComment() {
        try {
            Comment comment = new Comment(1L,1L, "Name","Anton", Calendar.getInstance().getTime());
            when(newsDAO.findDomainById(anyLong())).thenReturn(new News());
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


    @Test (expected = NumberFormatException.class)
    public void testDeleteAllCommentsFromNews(){
        try {
            doThrow(new NumberFormatException()).when(commentDAO).deleteCommentsByNewsId(anyLong());
            commentService.deleteAllCommentsFromNews(anyLong());
        } catch (DAOException|LogicException e) {
            LOG.error(e);
        }

    }
}
