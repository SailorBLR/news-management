package com.epam.hubarevich.service.impl;

import com.epam.hubarevich.dao.TagDAO;
import com.epam.hubarevich.dao.exception.DAOException;
import com.epam.hubarevich.domain.Tag;
import com.epam.hubarevich.service.TagService;
import com.epam.hubarevich.service.exception.LogicException;
import com.epam.hubarevich.utils.TagCheckUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service class to operate with Tag entities
 */

@Service
@Transactional
public class TagServiceImpl implements TagService {
    private final String MESSAGE_WRONG_TAG_FORMAT = "Tag is not valid";
    private final String MESSAGE_NO_TAG = "No such tag in the Database";
    private final String MESSAGE_NO_NEWS = "No such news message in the Database";

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

        if (!TagCheckUtil.checkTag(tag)){
            throw new LogicException(MESSAGE_WRONG_TAG_FORMAT);
        }
        try {
            tagDAO.create(tag);
        } catch (DAOException e) {
            throw new LogicException(e);
        }

    }

    @Override
    public void deleteTag(Long tagId) throws LogicException {
        if(tagId==null){
            throw new LogicException(MESSAGE_NO_TAG);
        }
        try {
            tagDAO.unwireTagsNewsByTags(tagId);
            tagDAO.delete(tagId);
        } catch (DAOException e) {
            throw new LogicException(e);
        }
    }

    @Override
    public List<Tag> findTagsByNewsId(Long newsId) throws LogicException {
        if(newsId==null) {
            throw new LogicException(MESSAGE_NO_NEWS);
        }
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
        if (!TagCheckUtil.checkTag(tag)){
            throw new LogicException(MESSAGE_WRONG_TAG_FORMAT);
        }
        try {
            tagDAO.update(tag);
        } catch (DAOException e) {
            throw new LogicException(e);
        }
    }
}
