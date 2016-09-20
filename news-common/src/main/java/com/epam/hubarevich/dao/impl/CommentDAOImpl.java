package com.epam.hubarevich.dao.impl;

import com.epam.hubarevich.dao.CommentDAO;
import com.epam.hubarevich.dao.exception.DAOException;
import com.epam.hubarevich.domain.Comment;
import com.epam.hubarevich.utils.DateUtil;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Implementation of CommentDAO. Contains methods realisations
 *
 * @author Anton_Hubarevich
 * @version 1.0
 */
@Repository
public class CommentDAOImpl implements CommentDAO {
    private final String SQL_FIND_ALL_COMMENTS =
            "SELECT comment_id,news_id,comment_text,comment_author,creation_date " +
                    "FROM comments";
    private final String SQL_CREATE_NEW_COMMENT =
            "INSERT INTO comments (comment_id,news_id,comment_text,comment_author,creation_date) " +
                    "VALUES (COMMENTS_SEQ.nextval,?,?,?,?)";
    private final String SQL_FIND_COMMENT_BY_ID =
            "SELECT comment_id,news_id,comment_text,comment_author,creation_date " +
                    "FROM comments " +
                    "WHERE comment_id=?";
    private final String SQL_FIND_COMMENT_BY_NEWS_ID =
            "SELECT comment_id,news_id,comment_text,comment_author,creation_date " +
                    "FROM comments " +
                    "WHERE news_id=? " +
                    "ORDER BY creation_date DESC ";
    private final String SQL_DELETE_COMMENT =
            "DELETE " +
                    "FROM comments " +
                    "WHERE comment_id=?";
    private final String SQL_DELETE_COMMENT_BY_NEW_ID =
            "DELETE " +
                    "FROM comments " +
                    "WHERE news_id=?";
    private final String SQL_UPDATE_COMMENT =
            "UPDATE comments " +
                    "SET comment_text=? " +
                    "WHERE comment_id=?";
    private final int PS_1 = 1;
    private final int PS_2 = 2;
    private final int PS_3 = 3;
    private final int PS_4 = 4;

    @Autowired
    private BasicDataSource dataSource;

    private Connection getConnection() {
        return DataSourceUtils.getConnection(dataSource);
    }

    @Override
    public List<Comment> findAll() throws DAOException {

        List<Comment> comments = new LinkedList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = getConnection();
            preparedStatement
                    = connection.prepareStatement(SQL_FIND_ALL_COMMENTS);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Comment comment = new Comment();
                configComment(resultSet, comment);
                comments.add(comment);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                throw new DAOException(e);
            }
            DataSourceUtils.releaseConnection(connection, dataSource);
        }
        return comments;
    }

    @Override
    public Comment findDomainById(Long id) throws DAOException {
        Comment comment = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = getConnection();
            preparedStatement
                    = connection.prepareStatement(SQL_FIND_COMMENT_BY_ID);
            preparedStatement.setLong(PS_1, id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                comment = new Comment();
                configComment(resultSet, comment);
            }

        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                throw new DAOException(e);
            }
            DataSourceUtils.releaseConnection(connection, dataSource);
        }
        return comment;
    }

    @Override
    public void delete(Long id) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = getConnection();
            preparedStatement
                    = connection.prepareStatement(SQL_DELETE_COMMENT);

            preparedStatement.setLong(PS_1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                throw new DAOException(e);
            }
            DataSourceUtils.releaseConnection(connection, dataSource);
        }
    }

    @Override
    public Long create(Comment comment) throws DAOException {
        Long commentId = 0L;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = getConnection();
            preparedStatement
                    = connection.prepareStatement(SQL_CREATE_NEW_COMMENT
                    , new String[]{COMMENTS_COLUMNS.COMMENT_ID.name()});
            preparedStatement.setLong(PS_1, comment.getNewsId());
            preparedStatement.setString(PS_2, comment.getCommentText());
            preparedStatement.setString(PS_3, comment.getCommentAuthor());
            if (comment.getCommentCreationDate() == null) {
                preparedStatement.setTimestamp(PS_4, DateUtil.makeTimeStampNow());
            } else {
                preparedStatement.setTimestamp(PS_4
                        , new Timestamp(comment.getCommentCreationDate().getTime()));
            }
            preparedStatement.executeUpdate();
            resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                commentId = resultSet.getLong(PS_1);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                throw new DAOException(e);
            }
            DataSourceUtils.releaseConnection(connection, dataSource);
        }
        return commentId;
    }

    @Override
    public void update(Comment comment) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = getConnection();
            preparedStatement
                    = connection.prepareStatement(SQL_UPDATE_COMMENT);
            preparedStatement.setString(PS_1, comment.getCommentText());
            preparedStatement.setLong(PS_2, comment.getCommentId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                throw new DAOException(e);
            }
            DataSourceUtils.releaseConnection(connection, dataSource);
        }
    }

    @Override
    public List<Comment> findCommentsByNewsId(Long newsId) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Comment> comments = new LinkedList<>();
        try {
            connection = getConnection();
            preparedStatement
                    = connection.prepareStatement(SQL_FIND_COMMENT_BY_NEWS_ID);
            preparedStatement.setLong(PS_1, newsId);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Comment comment = new Comment();
                configComment(resultSet, comment);
                comments.add(comment);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                throw new DAOException(e);
            }
            DataSourceUtils.releaseConnection(connection, dataSource);
        }
        return comments;
    }

    @Override
    public void deleteCommentsByNewsId(Long newsId) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dataSource.getConnection();
            preparedStatement
                     = connection.prepareStatement(SQL_DELETE_COMMENT_BY_NEW_ID);
            preparedStatement.setLong(PS_1, newsId);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new DAOException(e);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                throw new DAOException(e);
            }
            DataSourceUtils.releaseConnection(connection, dataSource);
        }
    }

    /**
     * Creates and configures the Comment object
     *
     * @param resultSet ResultSet object
     * @param comment   Comment object
     * @throws SQLException
     */

    private void configComment(ResultSet resultSet, Comment comment) throws SQLException {
        comment.setCommentId(resultSet.getLong(COMMENTS_COLUMNS.COMMENT_ID.name()));
        comment.setNewsId(resultSet.getLong(COMMENTS_COLUMNS.NEWS_ID.name()));
        comment.setCommentText(resultSet.getString(COMMENTS_COLUMNS.COMMENT_TEXT.name()));
        comment.setCommentAuthor(resultSet.getString(COMMENTS_COLUMNS.COMMENT_AUTHOR.name()));
        comment.setCommentCreationDate(resultSet.getTimestamp(COMMENTS_COLUMNS.CREATION_DATE.name()));
    }

    private enum COMMENTS_COLUMNS {
        COMMENT_ID, NEWS_ID, COMMENT_TEXT, COMMENT_AUTHOR, CREATION_DATE
    }
}
