package com.epam.hubarevich.service.impl;

import com.epam.hubarevich.dao.TagDAO;
import com.epam.hubarevich.dao.exception.DAOException;
import com.epam.hubarevich.domain.Tag;
import com.epam.hubarevich.service.TagService;
import com.epam.hubarevich.service.exception.LogicException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service class to operate with Tag entities
 */

@Component
@Transactional
public class TagServiceImpl implements TagService {
    @Autowired
    TagDAO tagDAO;

    @Override
    public List<Tag> getListOfTags() throws LogicException {
        List<Tag> tags;
        try {
            tags = tagDAO.findAll();
        } catch (DAOException e) {
            throw new LogicException(e);
        }
        return tags;
    }

    @Override
    public void addNewTag(Tag tag) throws LogicException {
        try {
            tagDAO.create(tag);
        } catch (DAOException e) {
            throw new LogicException(e);
        }

    }

    @Transactional
    @Override
    public void deleteTag(Long tagId) throws LogicException {
        try {
            tagDAO.unwireTagsNewsByTags(tagId);
            tagDAO.delete(tagId);
        } catch (DAOException e) {
            throw new LogicException(e);
        }
    }

    @Override
    public List<Tag> findTagsByNewsId(Long newsId) throws LogicException {
        List<Tag> tags;
        try {
            tags=tagDAO.getTagsByNewsId(newsId);
        } catch (DAOException e) {
            throw new LogicException(e);
        }
        return tags;
    }

    @Override
    public void updateTag(Tag tag) throws LogicException {
        try {
            tagDAO.update(tag);
        } catch (DAOException e) {
            throw new LogicException(e);
        }
    }
}
