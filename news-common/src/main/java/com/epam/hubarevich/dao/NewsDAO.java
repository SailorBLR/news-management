package com.epam.hubarevich.dao;

import com.epam.hubarevich.dao.exception.DAOException;
import com.epam.hubarevich.domain.Author;
import com.epam.hubarevich.domain.News;
import com.epam.hubarevich.domain.Tag;
import com.epam.hubarevich.domain.dto.SearchDTO;

import java.util.List;

/**
 * Interface for News message database operations
 * @author Anton_Hubarevich
 * @version 1.0
 */

public interface NewsDAO extends AbstractDAO<News>{
    /**
     * Adds Author object to News message object
     * @param newsId Long value of News message identifier
     * @param authorId Long value of Author identifier
     * @throws DAOException if SQL exception
     */
    void addNewsAuthor(Long newsId, Long authorId) throws DAOException;

    /**
     * Gets the Set of most commented news
     * @param newsQuantity int size of result Set
     * @return Set of News objects
     * @throws DAOException in case of SQL exception
     */
    List<News> getMostCommentedNews(int newsQuantity) throws DAOException;

    /**
     * Adds the Set of Tag objects to News message
     * @param newsId Long value of News message identifier
     * @param tags Set of Tag objects
     * @throws DAOException in case of SQL exception
     */
    void addTagsNews(Long newsId, List<Tag> tags) throws DAOException;

    /**
     * Searches for a News messages according to Author ID
     * @param author Author object
     * @return Set of News messages
     * @throws DAOException in case of SQL exception
     */
    List<News> findNewsByAuthor(Author author) throws DAOException;

    /**
     * Searches for a News messages according to Author ID
     * @param tags Set of Tag objects to search
     * @return Set of News messages
     * @throws DAOException in case of SQL exception
     */

    List<News> findNewsByTags(List<Tag> tags) throws DAOException;

    /**
     * Finds out, how many Newses is in the DB
     * @return int value
     * @throws DAOException in case of SQLException
     * @param searchDTO
     */
    int getTotalNewsQuantity(SearchDTO searchDTO) throws DAOException;

    /**
     * Searches for a paginated list of news according to the SearchCriteria
     * @param searchDTO SearchCriteria object
     * @param startIndex first row number of search result
     * @param finishIndex last row number of search result
     * @return List of news for defined page
     * @throws DAOException
     */
    List<News> getPaginatedListBySearchCriteria(SearchDTO searchDTO, int startIndex, int finishIndex)
            throws DAOException;


    /**
     * Searches for previous and next news id, according to SearchDto
     * @param searchDTO Object
     * @param newsId current news id
     * @throws DAOException in case of SQL exception
     */
    void getPrevNextIds(SearchDTO searchDTO,Long newsId) throws DAOException;

    /**
     * Searches for a news by title
     * @param news News object
     * @return News object if found
     * @throws DAOException in case of SQL exception
     */
    News getNewsByNewsTitle(News news) throws DAOException;



}
