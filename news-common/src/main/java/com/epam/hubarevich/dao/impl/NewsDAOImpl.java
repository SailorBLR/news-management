package com.epam.hubarevich.dao.impl;

import com.epam.hubarevich.dao.NewsDAO;
import com.epam.hubarevich.dao.exception.DAOException;
import com.epam.hubarevich.domain.Author;
import com.epam.hubarevich.domain.News;
import com.epam.hubarevich.domain.Tag;
import com.epam.hubarevich.domain.dto.SearchDTO;
import com.epam.hubarevich.utils.DateUtil;
import com.epam.hubarevich.utils.QueryBuilderUtil;
import com.epam.hubarevich.utils.StatementFillerUtil;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

/**
 * Implementation of NewsDAO. Contains methods realisations
 *
 * @author Anton_Hubarevich
 * @version 1.0
 */
@Repository
public class NewsDAOImpl implements NewsDAO {
    private final String SQL_CREATE_NEW_NEWS =
            "INSERT INTO news (news_id,title,short_text,full_text,creation_date,modification_date) " +
                    "VALUES (news_seq.nextval,?,?,?,?,?)";
    private final String SQL_DELETE_NEWS =
            "DELETE " +
                    "FROM news " +
                    "WHERE news_id = ?";
    private final String SQL_UPDATE_NEWS =
            "UPDATE news " +
                    "SET title=?,short_text=?,full_text=?,modification_date=? " +
                    "WHERE news_id=?";
    private final String SQL_FIND_NEWS_BY_ID =
            "SELECT news_id,title,short_text,full_text,creation_date,modification_date " +
                    "FROM news " +
                    "WHERE news_id = ?";
    private final String SQL_FIND_ALL_NEWS =
            "SELECT news_id,title,short_text,full_text,creation_date,modification_date " +
                    "FROM news";
    private final String SQL_ADD_AUTHOR_TO_NEWS =
            "INSERT INTO news_authors (news_id,author_id) " +
                    "VALUES (?,?)";
    private final String SQL_GET_MOST_COMMENTED =
            "SELECT * " +
                    "FROM (" +
                    "SELECT news.news_id,news.title, news.short_text, news.full_text," +
                    " news.creation_date, news.modification_date, NVL(num_comments, 0) AS num_comments " +
                    "FROM      news " +
                    " LEFT JOIN (" +
                    " SELECT   news_id, COUNT(*) AS num_comments " +
                    "FROM     comments " +
                    "GROUP BY news_id) " +
                    "cmt ON cmt.news_id = news.news_id " +
                    "       ORDER BY  num_comments DESC) " +
                    "WHERE ROWNUM<=?";
    private final String SQL_FIND_NEWS_BY_AUTHOR_ID =
            "SELECT N.news_id,N.title,N.short_text,N.full_text,N.creation_date,N.modification_date " +
                    "FROM news N " +
                    "LEFT JOIN news_authors NA " +
                    "ON NA.news_id=N.news_id " +
                    "WHERE NA.author_id=?";
    private final String SQL_FIND_NEWS_BY_TAGS =
            "SELECT DISTINCT N.news_id,N.title,N.short_text,N.full_text,N.creation_date,N.modification_date " +
                    "FROM news N " +
                    "LEFT JOIN news_tags NT " +
                    "ON NT.news_id=N.news_id  " +
                    "WHERE NT.tag_id=?";
    private final String SQL_ADD_TAGS_TO_NEWS =
            "INSERT INTO news_tags (news_id,tag_id) " +
                    "VALUES (?,?)";
    private final String SQL_ANOTHER_TAG = " OR NT.tag_id=? ";
    private final String SQL_FIND_NEWS_BY_TITLE =
            "SELECT news_id,title,short_text,full_text,creation_date,modification_date " +
                    "FROM news " +
                    "WHERE title = ?";

    private final int PS_1 = 1;
    private final int PS_2 = 2;
    private final int PS_3 = 3;
    private final int PS_4 = 4;
    private final int PS_5 = 5;

    @Autowired
    private BasicDataSource dataSource;
    @Autowired
    QueryBuilderUtil queryBuilderUtil;
    @Autowired
    StatementFillerUtil statementFillerUtil;

    private Connection getConnection() {
        return DataSourceUtils.getConnection(dataSource);
    }

    @Override
    public List<News> findAll() throws DAOException {
        List<News> newses = new LinkedList<>();
        News news;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = getConnection();
            preparedStatement
                    = connection.prepareStatement(SQL_FIND_ALL_NEWS);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                news = new News();
                configNews(resultSet, news);
                newses.add(news);
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
        return newses;
    }

    @Override
    public News findDomainById(Long id) throws DAOException {
        News news = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = getConnection();
            preparedStatement =
                    connection.prepareStatement(SQL_FIND_NEWS_BY_ID);
            preparedStatement.setLong(PS_1, id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                news = new News();
                configNews(resultSet, news);
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
        return news;
    }

    @Override
    public void delete(Long id) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = getConnection();
            preparedStatement
                    = connection.prepareStatement(SQL_DELETE_NEWS);
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
    public Long create(News news) throws DAOException {
        Long newsId = 0L;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = getConnection();
            preparedStatement
                    = connection.prepareStatement(SQL_CREATE_NEW_NEWS,
                    new String[]{NEWS_COLUMNS.NEWS_ID.name()});
            preparedStatement.setString(PS_1, news.getTitle());
            preparedStatement.setString(PS_2, news.getShortText());
            preparedStatement.setString(PS_3, news.getFullText());
            preparedStatement.setTimestamp(PS_4, DateUtil.makeTimeStampNow());
            preparedStatement.setTimestamp(PS_5, DateUtil.makeTimeStampNow());
            preparedStatement.executeUpdate();
            resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                newsId = resultSet.getLong(PS_1);
                news.setNewsId(newsId);
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
        return newsId;
    }

    @Override
    public void update(News news) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = getConnection();
            preparedStatement
                    = connection.prepareStatement(SQL_UPDATE_NEWS);
            preparedStatement.setString(PS_1, news.getTitle());
            preparedStatement.setString(PS_2, news.getShortText());
            preparedStatement.setString(PS_3, news.getFullText());
            preparedStatement.setDate(PS_4, new Date(Calendar.getInstance().getTimeInMillis()));
            preparedStatement.setLong(PS_5, news.getNewsId());
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
    public void addNewsAuthor(Long newsId, Long authorId) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = getConnection();
            preparedStatement
                    = connection.prepareStatement(SQL_ADD_AUTHOR_TO_NEWS);
            preparedStatement.setLong(PS_1, newsId);
            preparedStatement.setLong(PS_2, authorId);
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
    public List<News> getMostCommentedNews(int newsQuantity) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<News> newses = new LinkedList<>();
        try {
            connection = getConnection();
            preparedStatement
                    = connection.prepareStatement(SQL_GET_MOST_COMMENTED);
            preparedStatement.setLong(PS_1, newsQuantity);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                News news = new News();
                configNews(resultSet, news);
                newses.add(news);
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
        return newses;
    }

    @Override
    public void addTagsNews(Long newsId, List<Tag> tags) throws DAOException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = getConnection();
            preparedStatement
                    = connection.prepareStatement(SQL_ADD_TAGS_TO_NEWS);
            for (Tag tag : tags) {
                preparedStatement.setLong(PS_1, newsId);
                preparedStatement.setLong(PS_2, tag.getTagId());
                preparedStatement.executeUpdate();
            }
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
    public List<News> findNewsByAuthor(Author author) throws DAOException {
        List<News> newses = new LinkedList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = getConnection();
            preparedStatement
                    = connection.prepareStatement(SQL_FIND_NEWS_BY_AUTHOR_ID);
            preparedStatement.setLong(PS_1, author.getAuthorId());
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                News news = new News();
                configNews(resultSet, news);
                newses.add(news);
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
        return newses;
    }

    @Override
    public List<News> findNewsByTags(List<Tag> tags) throws DAOException {
        List<News> newses = new LinkedList<>();
        String query = SQL_FIND_NEWS_BY_TAGS;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        for (int i = 0; i < tags.size() - 1; i++) {
            query = query.concat(SQL_ANOTHER_TAG);
        }
        try {
            connection = getConnection();
            preparedStatement
                    = connection.prepareStatement(query);
            int i = 1;
            for (Tag tag : tags) {
                preparedStatement.setLong(i, tag.getTagId());
                i++;
            }
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                News news = new News();
                configNews(resultSet, news);
                newses.add(news);
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

        return newses;
    }


    @Override
    public int getTotalNewsQuantity(SearchDTO searchDTO) throws DAOException {
        int newsQuantity = 0;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = getConnection();
            preparedStatement
                    = connection.prepareStatement(queryBuilderUtil.buildNewsCountQuery(searchDTO));
            statementFillerUtil.statementPreparerCount(preparedStatement, searchDTO);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                newsQuantity = resultSet.getInt(PS_1);
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

        return newsQuantity;
    }

    @Override
    public List<News> getPaginatedListBySearchCriteria(SearchDTO searchDTO, int startIndex, int finishIndex)
            throws DAOException {
        List<News> newses = new LinkedList<>();
        String query = queryBuilderUtil.buildNewsSearchQuery(searchDTO);
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(query);
            statementFillerUtil.statementPreparerSearch(preparedStatement, searchDTO, startIndex, finishIndex);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                News news = new News();
                configNews(resultSet, news);
                newses.add(news);
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
        return newses;
    }

    @Override
    public void getPrevNextIds(SearchDTO searchDTO, Long newsId) throws DAOException {
        String query = queryBuilderUtil.buildPrevNextQuery(searchDTO);
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(query);

            int rownumber = getRownumByNewsId(searchDTO, newsId);
            searchDTO.setPrevId(null);
            searchDTO.setNextId(null);
            statementFillerUtil.prepareNextPrevFindQuery
                    (preparedStatement, searchDTO, rownumber);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                if (resultSet.getInt(PS_2) == (rownumber - 1)) {
                    searchDTO.setPrevId(resultSet.getLong(PS_1));
                }
                if (resultSet.getInt(PS_2) == (rownumber + 1)) {
                    searchDTO.setNextId(resultSet.getLong(PS_1));
                }
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

    }

    @Override
    public News getNewsByNewsTitle(News news) throws DAOException {
        News newsFound = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = getConnection();
            preparedStatement =
                    connection.prepareStatement(SQL_FIND_NEWS_BY_TITLE);
            preparedStatement.setString(PS_1, news.getTitle());
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                newsFound = new News();
                configNews(resultSet, newsFound);
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

        return newsFound;
    }

    private int getRownumByNewsId(SearchDTO searchDTO, Long newsId) throws SQLException {
        int rowNumber = 0;
        String query = queryBuilderUtil.buildRownumByIdQuery(searchDTO);
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = getConnection();
            preparedStatement =
                    connection.prepareStatement(query);
            statementFillerUtil.prepareRownumFindQuery(preparedStatement, searchDTO, newsId);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                rowNumber = resultSet.getInt(PS_2);
            }
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            DataSourceUtils.releaseConnection(connection, dataSource);
        }
        return rowNumber;
    }

    private void configNews(ResultSet resultSet, News news) throws SQLException {
        news.setNewsId(resultSet.getLong(NEWS_COLUMNS.NEWS_ID.name()));
        news.setTitle(resultSet.getString(NEWS_COLUMNS.TITLE.name()));
        news.setShortText(resultSet.getString(NEWS_COLUMNS.SHORT_TEXT.name()));
        news.setFullText(resultSet.getString(NEWS_COLUMNS.FULL_TEXT.name()));
        news.setNewsCreationDate(resultSet.getTimestamp(NEWS_COLUMNS.CREATION_DATE.name()));
        news.setNewsModificationDate(resultSet.getTimestamp(NEWS_COLUMNS.MODIFICATION_DATE.name()));
    }


    private enum NEWS_COLUMNS {
        NEWS_ID, TITLE, SHORT_TEXT, FULL_TEXT, CREATION_DATE, MODIFICATION_DATE
    }
}
