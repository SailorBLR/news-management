package com.epam.hubarevich.dao;

import com.epam.hubarevich.dao.exception.DAOException;
import com.epam.hubarevich.domain.Tag;

import java.util.List;

/**
 * Interface for Tag operations
 * @author Anton_Hubarevich
 * @version 1.0
 */
public interface TagDAO extends AbstractDAO<Tag> {
    /**
     * Searches for Tags according to News identifier
     * @param newsId Long value
     * @return Set of Tags
     * @throws DAOException in case of SQL exception
     */
    List<Tag> getTagsByNewsId(Long newsId) throws DAOException;


    /**
     * Deletes dependencies tag-news from table Tags-News
     * @param newsId Long value
     * @throws DAOException if SQLException obtained
     */
    void unwireTagsNewsByNews(Long newsId) throws DAOException;

    
  /**
   * Deletes dependencies tag-news from table Tags-News
   * @param tagId
   * @throws DAOException
   */
    void unwireTagsNewsByTags(Long tagId) throws DAOException;
}
