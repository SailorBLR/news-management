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
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Implementation of NewsDAO. Contains methods realisations
 *
 * @author Anton_Hubarevich
 * @version 1.0
 */
@Component
public class NewsDAOImpl implements NewsDAO {
    private final String SQL_CREATE_NEW_NEWS = "INSERT INTO news (news_id,title,short_text,full_text,creation_date,modification_date) VALUES (news_seq.nextval,?,?,?,?,?)";
    private final String SQL_DELETE_NEWS = "DELETE FROM news WHERE news_id = ?";
    private final String SQL_UPDATE_NEWS = "UPDATE news SET title=?,short_text=?,full_text=?,modification_date=? WHERE news_id=?";

    private final String SQL_FIND_NEWS_BY_ID = "SELECT news_id,title,short_text,full_text,creation_date,modification_date FROM news WHERE news_id = ?";
    private final String SQL_FIND_ALL_NEWS = "SELECT news_id,title,short_text,full_text,creation_date,modification_date FROM news";
    private final String SQL_ADD_AUTHOR_TO_NEWS = "INSERT INTO news_authors (news_id,author_id) VALUES (?,?)";
    private final String SQL_GET_MOST_COMMENTED = "SELECT * FROM (SELECT news.news_id,news.title, news.short_text, news.full_text," +
            "            news.creation_date, news.modification_date, NVL(num_comments, 0) AS num_comments FROM      news " +
            "            LEFT JOIN (SELECT   news_id, COUNT(*) AS num_comments FROM     comments GROUP BY news_id) cmt on cmt.news_id = news.news_id " +
            "            ORDER BY  num_comments DESC) WHERE ROWNUM<=?";
    private final String SQL_FIND_NEWS_BY_AUTHOR_ID = "SELECT N.news_id,N.title,N.short_text,N.full_text,N.creation_date,N.modification_date FROM news N " +
            "LEFT JOIN news_authors NA on NA.news_id=N.news_id WHERE NA.author_id=?";
    private final String SQL_FIND_NEWS_BY_TAGS = "SELECT DISTINCT N.news_id,N.title,N.short_text,N.full_text,N.creation_date,N.modification_date FROM news N " +
            "            LEFT JOIN news_tags NT on NT.news_id=N.news_id  WHERE NT.tag_id=?";
    private final String SQL_ADD_TAGS_TO_NEWS = "INSERT INTO news_tags (news_id,tag_id) VALUES (?,?)";
    private final String SQL_ANOTHER_TAG = " OR NT.tag_id=? ";
    private final String SQL_FIND_NEWS_BY_TITLE = "SELECT news_id,title,short_text,full_text,creation_date,modification_date FROM news WHERE title = ?";

    @Autowired
    private BasicDataSource dataSource;
    @Autowired
    QueryBuilderUtil queryBuilderUtil;
    @Autowired
    StatementFillerUtil statementFillerUtil;

    @Override
    public List<News> findAll() throws DAOException {
        List<News> newses = new LinkedList<>();
        News news;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement
                     = connection.prepareStatement(SQL_FIND_ALL_NEWS)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                news = new News();
                configNews(resultSet, news);
                newses.add(news);
            }

        } catch (SQLException e) {
            throw new DAOException(e);
        }
        return newses;
    }

    @Override
    public News findDomainById(Long id) throws DAOException {

        News news = null;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement =
                     connection.prepareStatement(SQL_FIND_NEWS_BY_ID)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                news = new News();
                configNews(resultSet, news);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
        return news;
    }

    @Override
    public void delete(Long id) throws DAOException {

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement
                     = connection.prepareStatement(SQL_DELETE_NEWS)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }


    @Override
    public Long create(News news) throws DAOException {

        Long newsId = 0L;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement
                     = connection.prepareStatement(SQL_CREATE_NEW_NEWS,
                     new String[]{NEWS_COLUMNS.NEWS_ID.name()})) {
            preparedStatement.setString(1, news.getTitle());
            preparedStatement.setString(2, news.getShortText());
            preparedStatement.setString(3, news.getFullText());
            preparedStatement.setTimestamp(4, DateUtil.makeTimeStampNow());
            preparedStatement.setTimestamp(5, DateUtil.makeTimeStampNow());
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                newsId = resultSet.getLong(1);
                news.setNewsId(newsId);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
        return newsId;
    }

    @Override
    public void update(News news) throws DAOException {

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement
                     = connection.prepareStatement(SQL_UPDATE_NEWS)) {
            preparedStatement.setString(1, news.getTitle());
            preparedStatement.setString(2, news.getShortText());
            preparedStatement.setString(3, news.getFullText());
            preparedStatement.setDate(4, new Date(news.getNewsModificationDate().getTime()));
            preparedStatement.setLong(5, news.getNewsId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    @Override
    public void addNewsAuthor(Long newsId, Long authorId) throws DAOException {

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement
                     = connection.prepareStatement(SQL_ADD_AUTHOR_TO_NEWS)) {
            preparedStatement.setLong(1, newsId);
            preparedStatement.setLong(2, authorId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException(e);
        }

    }

    @Override
    public List<News> getMostCommentedNews(int newsQuantity) throws DAOException {

        List<News> newses = new LinkedList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement
                     = connection.prepareStatement(SQL_GET_MOST_COMMENTED)) {
            preparedStatement.setLong(1, newsQuantity);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                News news = new News();
                configNews(resultSet, news);
                newses.add(news);
            }

        } catch (SQLException e) {
            throw new DAOException(e);
        }
        return newses;
    }

    @Override
    public void addTagsNews(Long newsId, List<Tag> tags) throws DAOException {

        try (Connection connection = dataSource.getConnection(); PreparedStatement preparedStatement
                = connection.prepareStatement(SQL_ADD_TAGS_TO_NEWS)) {
            for (Tag tag : tags) {
                preparedStatement.setLong(1, newsId);
                preparedStatement.setLong(2, tag.getTagId());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    @Override
    public List<News> findNewsByAuthor(Author author) throws DAOException {
        List<News> newses = new LinkedList<>();
        try (Connection connection = dataSource.getConnection(); PreparedStatement preparedStatement
                = connection.prepareStatement(SQL_FIND_NEWS_BY_AUTHOR_ID)) {
            preparedStatement.setLong(1, author.getAuthorId());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                News news = new News();
                configNews(resultSet, news);
                newses.add(news);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
        return newses;
    }

    @Override
    public List<News> findNewsByTags(List<Tag> tags) throws DAOException {
        List<News> newses = new LinkedList<>();
        String query = SQL_FIND_NEWS_BY_TAGS;
        for (int i = 0; i < tags.size() - 1; i++) {
            query = query.concat(SQL_ANOTHER_TAG);
        }
        try (Connection connection = dataSource.getConnection(); PreparedStatement preparedStatement
                = connection.prepareStatement(query)) {
            int i = 1;
            for (Tag tag : tags) {
                preparedStatement.setLong(i, tag.getTagId());
                i++;
            }
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                News news = new News();
                configNews(resultSet, news);
                newses.add(news);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }

        return newses;
    }


    @Override
    public int getTotalNewsQuantity(SearchDTO searchDTO) throws DAOException {
        int newsQuantity = 0;
        try (Connection connection = dataSource.getConnection(); PreparedStatement preparedStatement
                = connection.prepareStatement(queryBuilderUtil.buildNewsCountQuery(searchDTO))) {

            statementFillerUtil.statementPreparerCount(preparedStatement, searchDTO);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                newsQuantity = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }

        return newsQuantity;
    }

    @Override
    public List<News> getPaginatedListBySearchCriteria(SearchDTO searchDTO, int startIndex, int finishIndex)
            throws DAOException {
        List<News> newses = new LinkedList<>();
        String query = queryBuilderUtil.buildNewsSearchQuery(searchDTO);
        try (Connection connection = dataSource.getConnection(); PreparedStatement preparedStatement
                = connection.prepareStatement(query)) {
            statementFillerUtil.statementPreparerSearch(preparedStatement, searchDTO, startIndex, finishIndex);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                News news = new News();
                configNews(resultSet, news);
                newses.add(news);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
        return newses;
    }

    @Override
    public void getPrevNextIds(SearchDTO searchDTO, Long newsId) throws DAOException {
        String query = queryBuilderUtil.buildPrevNextQuery(searchDTO);
        try (Connection connection = dataSource.getConnection(); PreparedStatement preparedStatement =
                connection.prepareStatement(query)) {
            int rownumber = getRownumByNewsId(searchDTO, newsId);
            searchDTO.setPrevId(null);
            searchDTO.setNextId(null);
            statementFillerUtil.prepareNextPrevFindQuery
                    (preparedStatement, searchDTO, rownumber);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                if (resultSet.getInt(2) == (rownumber - 1)) {
                    searchDTO.setPrevId(resultSet.getLong(1));
                }
                if (resultSet.getInt(2) == (rownumber + 1)) {
                    searchDTO.setNextId(resultSet.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }

    }

    @Override
    public News getNewsByNewsTitle(News news) throws DAOException {
        News newsFound = null;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement =
                     connection.prepareStatement(SQL_FIND_NEWS_BY_TITLE)) {
            preparedStatement.setString(1, news.getTitle());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                newsFound = new News();
                configNews(resultSet, newsFound);
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
        return newsFound;
    }

    private int getRownumByNewsId(SearchDTO searchDTO, Long newsId) throws SQLException {

        int rowNumber = 0;
        String query = queryBuilderUtil.buildRownumByIdQuery(searchDTO);
        try (Connection connection = dataSource.getConnection(); PreparedStatement preparedStatement =
                connection.prepareStatement(query)) {
            statementFillerUtil.prepareRownumFindQuery(preparedStatement, searchDTO, newsId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                rowNumber = resultSet.getInt(2);
            }
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


    public enum NEWS_COLUMNS {
        NEWS_ID, TITLE, SHORT_TEXT, FULL_TEXT, CREATION_DATE, MODIFICATION_DATE
    }
}
