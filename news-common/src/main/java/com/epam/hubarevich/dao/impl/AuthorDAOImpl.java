package com.epam.hubarevich.dao.impl;

import com.epam.hubarevich.dao.AuthorDAO;
import com.epam.hubarevich.dao.exception.DAOException;
import com.epam.hubarevich.domain.Author;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Implementation of AuthorDAO. Contains methods realisations
 *
 * @author Anton_Hubarevich
 * @version 1.0
 */
@Component
public class AuthorDAOImpl implements AuthorDAO {
    private final String SQL_CREATE_AUTHOR = "INSERT INTO authors (author_id,author_name) VALUES " +
            "(AUTHORS_SEQ.nextval,?)";
    private final String SQL_DELETE_AUTHOR = "DELETE FROM authors WHERE author_id = ?";
    private final String SQL_UPDATE_AUTHOR_START = "UPDATE authors SET author_name = ?";
    private final String SQL_UPDATE_AUTHOR_END = " where author_id = ?";
    private final String SQL_ADD_EXPIRE_BLOCK = ",expired = ?";
    private final String SQL_FIND_AUTHOR_BY_ID = "SELECT author_name,author_id,expired FROM authors WHERE author_id=?";
    private final String SQL_FIND_AUTHOR_BY_NAME = "SELECT author_name,author_id,expired FROM authors WHERE author_name=?";
    private final String SQL_FIND_ALL_AUTHORS = "SELECT author_id,author_name,expired FROM authors";
    private final String SQL_FIND_AUTHOR_BY_NEWS_ID = "SELECT A.author_id,A.author_name,A.expired FROM authors A LEFT OUTER JOIN news_authors NA " +
            "ON NA.author_id = A.AUTHOR_ID LEFT OUTER JOIN news N ON N.news_id=NA.news_id WHERE N.news_id=?";
    private final String SQL_DELETE_DEPENDENCY_AUTHOR_NEWS = "DELETE FROM news_authors WHERE news_id = ?";
    private final String SQL_GET_AVAILABLE_AUTHORS = "SELECT author_id,author_name,expired FROM authors WHERE expired IS NULL";

    @Autowired
    private BasicDataSource dataSource;

    /**
     * @return List of all Authors from database
     * @throws DAOException if SQLException thrown
     */
    @Override
    public List<Author> findAll() throws DAOException {
        Author author;
        List<Author> authors = new LinkedList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement
                     = connection.prepareStatement(SQL_FIND_ALL_AUTHORS)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.isBeforeFirst()) {
                while (resultSet.next()) {
                    author = new Author();
                    configAuthor(resultSet, author);
                    authors.add(author);
                }
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
        return authors;
    }


    @Override
    public Author findDomainById(Long id) throws DAOException {
        Author author = null;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement
                     = connection.prepareStatement(SQL_FIND_AUTHOR_BY_ID)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                author = new Author();
                configAuthor(resultSet, author);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
        return author;
    }

    @Override
    public void delete(Long id) throws DAOException {

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement =
                     connection.prepareStatement(SQL_DELETE_AUTHOR)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    @Override
    public Long create(Author author) throws DAOException {
        Long authorId = 0L;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement =
                     connection.prepareStatement(SQL_CREATE_AUTHOR,
                             new String[]{AUTHOR_COLUMNS.AUTHOR_ID.name()})) {
            preparedStatement.setString(1, author.getAuthorName());
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                authorId = resultSet.getLong(1);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
        return authorId;
    }

    @Override
    public void update(Author author) throws DAOException {
        String query = SQL_UPDATE_AUTHOR_START;
        if (author.getExpired() != null) {
            query = query.concat(SQL_ADD_EXPIRE_BLOCK);
        }
        query=query.concat(SQL_UPDATE_AUTHOR_END);

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement =
                     connection.prepareStatement(query)) {
            preparedStatement.setString(1, author.getAuthorName());

            if (author.getExpired() != null) {
                preparedStatement.setTimestamp(2, new Timestamp(author.getExpired().getTime()));
                preparedStatement.setLong(3, author.getAuthorId());
            } else {
                preparedStatement.setLong(2, author.getAuthorId());
            }
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    @Override
    public Author findAuthorByName(String authorName) throws DAOException {
        Author author = null;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement
                     = connection.prepareStatement(SQL_FIND_AUTHOR_BY_NAME)) {
            preparedStatement.setString(1, authorName);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                author = new Author();
                configAuthor(resultSet, author);
            }
        } catch (SQLException e) {
            throw new DAOException(e);

        }
        return author;
    }

    @Override
    public Author findAuthorByNewsId(Long newsId) throws DAOException {
        Author author = null;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement =
                     connection.prepareStatement(SQL_FIND_AUTHOR_BY_NEWS_ID)) {
            preparedStatement.setLong(1, newsId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.isBeforeFirst()) {
                while (resultSet.next()) {
                    author = new Author();
                    configAuthor(resultSet, author);
                }
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
        return author;
    }

    @Override
    public void unwireNewsAuthors(Long newsId) throws DAOException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement =
                     connection.prepareStatement(SQL_DELETE_DEPENDENCY_AUTHOR_NEWS)) {
            preparedStatement.setLong(1, newsId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    @Override
    public List<Author> getAvailableAuthors() throws DAOException {
        Author author;
        List<Author> authors = new LinkedList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement
                     = connection.prepareStatement(SQL_GET_AVAILABLE_AUTHORS)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.isBeforeFirst()) {
                while (resultSet.next()) {
                    author = new Author();
                    configAuthor(resultSet, author);
                    authors.add(author);
                }
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
        return authors;
    }

    private void configAuthor(ResultSet resultSet, Author author) throws SQLException {
        author.setAuthorId(resultSet.getLong(AUTHOR_COLUMNS.AUTHOR_ID.name()));
        author.setAuthorName(resultSet.getString(AUTHOR_COLUMNS.AUTHOR_NAME.name()));
        if (resultSet.getTimestamp(AUTHOR_COLUMNS.EXPIRED.name()) != null) {
            author.setExpired(resultSet.getTimestamp(AUTHOR_COLUMNS.EXPIRED.name()));
        }

    }

    public enum AUTHOR_COLUMNS {
        AUTHOR_ID, AUTHOR_NAME, EXPIRED
    }
}
