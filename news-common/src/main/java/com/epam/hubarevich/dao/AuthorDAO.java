package com.epam.hubarevich.dao;

import com.epam.hubarevich.dao.exception.DAOException;
import com.epam.hubarevich.domain.Author;

import java.util.List;

/**
 * Interface for Author database operations
 * @author Anton_Hubarevich
 * @version 1.0
 */
public interface AuthorDAO extends AbstractDAO <Author>{

    /**
     * Searches for database field with the defined AUTHOR_NAME
     * @param authorName String value of AUTHOR_NAME
     * @return Author object
     * @throws DAOException in case of SQL exception
     */
    Author findAuthorByName(String authorName) throws DAOException;

    /**
     * Searches for database field based on NEWS_ID
     * @param newsId Long value of NEWS_ID unique identifier
     * @return Author object
     * @throws DAOException in case of SQL exception
     */
    Author findAuthorByNewsId(Long newsId) throws DAOException;

    /**
     * Deletes dependency news-author from table News-Authors
     * @param newsId Long value
     * @throws DAOException while SQLException obtained
     */
    void unwireNewsAuthors(Long newsId) throws DAOException;

    /**
     * Gets List of the all available authors
     * @return List<Author></Author>
     * @throws DAOException
     */
    List<Author> getAvailableAuthors() throws DAOException;
}
