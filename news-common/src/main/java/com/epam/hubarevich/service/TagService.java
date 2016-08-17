package com.epam.hubarevich.service;

import com.epam.hubarevich.domain.Tag;
import com.epam.hubarevich.service.exception.LogicException;

import java.util.List;

/**
 * Created by Anton_Hubarevich on 7/18/2016.
 */
public interface TagService {
    /**
     * Serches for a list of Tags
     * @return list Tags
     * @throws LogicException
     */
    List<Tag> getListOfTags () throws LogicException;

    /**
     * Adds new tag
     * @param tag Tag
     * @throws LogicException
     */
    void addNewTag(Tag tag) throws LogicException;

    /**
     * Deletes tag from the DB
     * @param tagId Long identifier
     * @throws LogicException
     */
    void deleteTag(Long tagId) throws LogicException;

    /**
     * Searches for a list of Tag by NewsId
     * @param newsId Long
     * @return List<Tag>
     * @throws LogicException
     */
    List<Tag> findTagsByNewsId (Long newsId) throws LogicException;

    void updateTag(Tag tag) throws LogicException;
}
