package com.epam.hubarevich.dao.impl;

import com.epam.hubarevich.dao.TagDAO;
import com.epam.hubarevich.dao.exception.DAOException;
import com.epam.hubarevich.domain.Tag;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * Implementation of TagDAO. Contains methods realisations
 *
 * @author Anton_Hubarevich
 * @version 1.0
 */
@Component
public class TagDAOImpl implements TagDAO {

    private final String SQL_FIND_TAGS_BY_NEWS_ID = "SELECT T.TAG_ID,T.TAG_NAME FROM tags T LEFT JOIN news_tags NT on NT.TAG_ID = T.TAG_ID WHERE NT.NEWS_ID=?";
    private final String SQL_FIND_TAG_BY_ID = "SELECT tag_id,tag_name FROM tags WHERE tag_id=?";
    private final String SQL_DELETE_TAG_NEWS_DEPENDENCY_NEWS = "DELETE FROM news_tags WHERE news_id= ?";
    private final String SQL_DELETE_TAG_NEWS_DEPENDENCY_TAGS = "DELETE FROM news_tags WHERE tag_id= ?";
    private final String SQL_DELETE_TAG = "DELETE FROM tags WHERE tag_id=?";
    private final String SQL_FIND_ALL_TAGS = "SELECT tag_id,tag_name FROM tags";
    private final String SQL_ADD_NEW_TAG = "INSERT INTO tags (tag_id,tag_name) VALUES (tags_sq.nextval,?)";
    private final String SQL_UPDATE_TAG = "UPDATE tags SET tag_name=? WHERE tag_id=?";

    @Autowired
    private BasicDataSource dataSource;

    @Override
    public List<Tag> findAll() throws DAOException {
        List<Tag> tags = new LinkedList<>();
        try (Connection connection=dataSource.getConnection();
        PreparedStatement preparedStatement
        =connection.prepareStatement(SQL_FIND_ALL_TAGS)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Tag tag = new Tag();
                configTag(resultSet,tag);
                tags.add(tag);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
        return tags;
    }

    @Override
    public Tag findDomainById(Long id) throws DAOException {
        Tag tag = null;
        try (Connection connection= dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_TAG_BY_ID)) {
            preparedStatement.setLong(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                tag = new Tag();
                configTag(resultSet,tag);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
        return tag;
    }

    @Override
    public void delete(Long tagId) throws DAOException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement
                     = connection.prepareStatement(SQL_DELETE_TAG)) {
            preparedStatement.setLong(1, tagId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    @Override
    public Long create(Tag tag) throws DAOException {
        Long tagId = 0L;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement =
                     connection.prepareStatement(SQL_ADD_NEW_TAG,
                             new String[]{TAG_COLUMNS.TAG_ID.name()})) {
            preparedStatement.setString(1, tag.getTagName());
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                tagId = resultSet.getLong(1);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
        return tagId;
    }

    @Override
    public void update(Tag tag) throws DAOException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement =
                     connection.prepareStatement(SQL_UPDATE_TAG)) {
            preparedStatement.setString(1, tag.getTagName());
            preparedStatement.setLong(2, tag.getTagId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    @Override
    public List<Tag> getTagsByNewsId(Long newsId) throws DAOException {

        List<Tag> tags = new LinkedList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement
                     = connection.prepareStatement(SQL_FIND_TAGS_BY_NEWS_ID)) {
            preparedStatement.setLong(1, newsId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Tag tag = new Tag();
                configTag(resultSet, tag);
                tags.add(tag);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
        return tags;
    }

    @Override
    public void unwireTagsNewsByNews(Long newsId) throws DAOException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement
                     = connection.prepareStatement(SQL_DELETE_TAG_NEWS_DEPENDENCY_NEWS)) {
            preparedStatement.setLong(1, newsId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    @Override
    public void unwireTagsNewsByTags(Long tagId) throws DAOException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement
                     = connection.prepareStatement(SQL_DELETE_TAG_NEWS_DEPENDENCY_TAGS)) {
            preparedStatement.setLong(1, tagId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }



    private void configTag(ResultSet resultSet, Tag tag) throws SQLException {
        tag.setTagId(resultSet.getLong(TAG_COLUMNS.TAG_ID.name()));
        tag.setTagName(resultSet.getString(TAG_COLUMNS.TAG_NAME.name()));
    }

    public enum TAG_COLUMNS {
        TAG_ID, TAG_NAME
    }
}
