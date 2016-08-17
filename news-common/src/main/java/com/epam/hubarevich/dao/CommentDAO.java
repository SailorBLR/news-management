package com.epam.hubarevich.dao;

import com.epam.hubarevich.dao.exception.DAOException;
import com.epam.hubarevich.domain.Comment;

import java.util.List;

/**
 * Interface for Comment database operations
 * @author Anton_Hubarevich
 * @version 1.0
 */

public interface CommentDAO extends AbstractDAO<Comment> {

    /**
     * Searches for a database fields based on NEWS_ID
     * @param newsId Long value of unique News identifier
     * @return Set of comments for defined News message
     * @throws DAOException in case of SQL exception
     */
    List<Comment> findCommentsByNewsId(Long newsId) throws DAOException;


    /**
     * Searches and delete all comments by News_id
     * @param newsId Long
     * @throws DAOException
     */
    void deleteCommentsByNewsId(Long newsId) throws DAOException;
}
