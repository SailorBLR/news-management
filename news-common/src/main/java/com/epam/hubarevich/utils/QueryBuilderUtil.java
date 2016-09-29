package com.epam.hubarevich.utils;

import com.epam.hubarevich.domain.Tag;
import com.epam.hubarevich.domain.dto.SearchDTO;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class QueryBuilderUtil {

    private final String SQL_SEARCH_QUERY_BASE =
            "SELECT N.news_id,N.title,N.short_text,N.full_text,N.creation_date, N.modification_date,COUNT(C.NEWS_ID) AS num_comments " +
            "FROM news N ";
    private final String SQL_QUERY_JOIN_COMMENTS_GROUP =
            " LEFT JOIN comments C " +
            "ON C.news_id=N.news_id";
    private final String SQL_ORDER_BY =
            " GROUP BY N.news_id, N.title, N.short_text, N.full_text, N.creation_date, N.modification_date " +
            "ORDER BY N.MODIFICATION_DATE DESC, num_comments DESC";
    private final String SQL_PAGINATION_START =
            "SELECT N.*" +
            "FROM (SELECT ROWNUM rw, N.* " +
            "FROM (";
    private final String SQL_SEARCH_QUERY_END =
            ") N where rownum <=?" +
            " ) N" +
            " where N.rw >= ?";
    private final String SQL_JOIN_AUTHORS =
            "LEFT JOIN news_authors NA " +
            "ON NA.news_id = N.news_id";
    private final String SQL_AND = " OR ";
    private final String SQL_JOIN_TAGS =
            " LEFT JOIN news_tags NT " +
            "ON NT.news_id=N.news_id";
    private final String SQL_ANOTHER_TAG =
            " OR NT.tag_id=? ";
    private final String SQL_WHERE = " WHERE ";
    private final String SQL_WHERE_AUTHOR = "NA.author_id=? ";
    private final String SQL_WHERE_TAG = " (NT.tag_id=?";
    private final String SQL_END = ") ";
    private final String SQL_COUNT =
            "SELECT COUNT (*) " +
            "FROM(";
    private final String SQL_CURRENT_NEWS_ROWNUM =
            "SELECT N.news_id,rw " +
            "FROM (" +
                    "SELECT ROWNUM rw, N.* " +
                    "FROM (";
    private final String SQL_NEXT_PREV_END =
            ") N) N " +
            "WHERE rw=? or rw=?";
    private final String SQL_ROWNUM_BY_ID_END =
            ") N) N " +
            "WHERE news_id=?";

    /**
     * Builds Search criteria query
     *
     * @param searchDTO gets the SearchDTO object
     * @return builded query
     */
    private String buildSearchCriteriaQuery(SearchDTO searchDTO) {

        String query = SQL_SEARCH_QUERY_BASE;
        boolean author = false;
        boolean tags = false;
        /**
         * Checks if the Criteria contains Author object
         */
        if (searchDTO.getAuthor() != null) {
            author = true;
            query = query.concat(SQL_JOIN_AUTHORS);
        }

        /**
         * Checks if the Criteria contains List<Tag>
         */
        if (searchDTO.getTags() != null) {
            tags = true;
            query = query.concat(SQL_JOIN_TAGS);
        }

        query = query.concat(SQL_QUERY_JOIN_COMMENTS_GROUP);
        /**
         * Checks if the "while" clause needed
         */

        if (author || tags) {
            query = query.concat(SQL_WHERE);
        }

        if (author) {
            query = query.concat(SQL_WHERE_AUTHOR);
        }
        if (author && tags) {
            query = query.concat(SQL_AND);
        }
        if (tags) {
            query = query.concat(SQL_WHERE_TAG);
            List<Tag> tagsList = searchDTO.getTags();
            for (int i = 0; i < tagsList.size() - 1; i++) {
                query = query.concat(SQL_ANOTHER_TAG);
            }
            query = query.concat(SQL_END);
        }
        query = query.concat(SQL_ORDER_BY);
        return query;
    }

    /**
     * Builds the Query for search by search criteria
     * @param searchDTO Search Criteria object
     * @return query line
     */
    public String buildNewsSearchQuery(SearchDTO searchDTO) {
        String query = SQL_PAGINATION_START;
        query = query.concat(buildSearchCriteriaQuery(searchDTO));
        query = query.concat(SQL_SEARCH_QUERY_END);
        return query;
    }

    /**
     * Builds query for counting of search results
     * @param searchDTO Search Criteria object
     * @return String query line
     */

    public String buildNewsCountQuery(SearchDTO searchDTO) {
        String query = SQL_COUNT;
        query = query.concat(buildSearchCriteriaQuery(searchDTO));
        query = query.concat(SQL_END);
        return query;
    }

    public String buildRownumByIdQuery (SearchDTO searchDTO) {
        String query = SQL_CURRENT_NEWS_ROWNUM;
        query = query.concat(buildSearchCriteriaQuery(searchDTO));
        query = query.concat(SQL_ROWNUM_BY_ID_END);
        return query;
    }

    /**
     * Builds the query for Next and Previous page search
     * @param searchDTO Search criteria object
     * @return String query
     */
    public String buildPrevNextQuery (SearchDTO searchDTO) {
        String query = SQL_CURRENT_NEWS_ROWNUM;
        query = query.concat(buildSearchCriteriaQuery(searchDTO));
        query = query.concat(SQL_NEXT_PREV_END);
        return query;
    }

}
