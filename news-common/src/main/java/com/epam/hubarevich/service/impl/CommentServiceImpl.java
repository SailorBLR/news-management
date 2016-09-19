package com.epam.hubarevich.service.impl;

import com.epam.hubarevich.dao.CommentDAO;
import com.epam.hubarevich.dao.NewsDAO;
import com.epam.hubarevich.dao.exception.DAOException;
import com.epam.hubarevich.domain.Comment;
import com.epam.hubarevich.service.CommentService;
import com.epam.hubarevich.service.exception.LogicException;
import com.epam.hubarevich.utils.CommentCheckUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Anton_Hubarevich on 6/27/2016.
 */
@Service
@Transactional
public class CommentServiceImpl implements CommentService {

    @Autowired
    CommentDAO commentDAO;

    @Autowired
    NewsDAO newsDAO;

    @Override
    public Long addComment(Comment comment) throws LogicException {
        Long commentId = 0L;

        if (CommentCheckUtil.checkComment(comment)) {
            try {
                comment.setNews(newsDAO.findDomainById(comment.getNewsId()));
                commentId=commentDAO.create(comment);
            } catch (DAOException e) {
                throw new LogicException(e);
            }
        }
        return commentId;
    }

    @Override
    public void deleteComment(Long commentId)  throws LogicException{
        if (commentId>0) {
            try {
                commentDAO.delete(commentId);
            } catch (DAOException e) {
                throw new LogicException(e);
            }
        }
    }

    @Override
    public void updateComment(Comment comment)  throws LogicException{
        if (CommentCheckUtil.checkComment(comment)) {
            try {
                commentDAO.update(comment);
            } catch (DAOException e) {
                throw new LogicException(e);
            }
        }
    }

    @Override
    @Deprecated
    public void deleteAllCommentsFromNews(Long newsId) throws LogicException {

        try {
            commentDAO.deleteCommentsByNewsId(newsId);
        } catch (DAOException e) {
            throw new LogicException(e);
        }
    }
}
