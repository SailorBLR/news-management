package com.epam.hubarevich.dao.util;

import com.epam.hubarevich.domain.Tag;
import com.epam.hubarevich.domain.dto.SearchDTO;
import org.hibernate.Query;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Anton_Hubarevich on 9/2/2016.
 */
@Component
public class HQLQueryBuilderUtil {

    private final String HQL_PAGINATION_START = "SELECT DISTINCT N FROM News N ";
    private final String HQL_JOIN_AUTHORS = "LEFT JOIN N.authors NA ";
    private final String HQL_JOIN_TAGS = "LEFT JOIN N.tags NT ";
    private final String HQL_WHERE = "WHERE ";
    private final String HQL_OR = "OR ";
    private final String HQL_WHERE_AUTHOR = ":author IN NA ";
    private final String HQL_WHERE_TAGS = "NT IN :tags ";
    private final String HQL_SEARCH_QUERY_END = "group by N.newsId,N.title,N.shortText,N.fullText,N.newsCreationDate, N.newsModificationDate " +
            " order by N.comments.size desc, N.newsModificationDate DESC";

    private final String HQL_QUANTITY_START = "SELECT COUNT (N) FROM News N ";
    private final String HQL_CURRENT_NEWS_ROWNUM_START = "SELECT N.newsId FROM News N ";



    /**
     * Builds Search criteria query
     *
     * @param searchDTO gets the SearchDTO object
     * @return builded query
     * , N.newsModificationDate
     */
    private String buildSearchCriteriaQuery(SearchDTO searchDTO) {

        String query = "";
        boolean author = false;
        boolean tags = false;
        /**
         * Checks if the Criteria contains Author object
         */
        if (searchDTO.getAuthor() != null) {
            author = true;
            query = query.concat(HQL_JOIN_AUTHORS);
        }

        /**
         * Checks if the Criteria contains List<Tag>
         */
        if (searchDTO.getTags() != null) {
            tags = true;
            query = query.concat(HQL_JOIN_TAGS);
        }
        /**
         * Checks if the "while" clause needed
         */

        if (author | tags) {
            query = query.concat(HQL_WHERE);
        }

        if (author) {
            query = query.concat(HQL_WHERE_AUTHOR);
        }
        if (author && tags) {
            query = query.concat(HQL_OR);
        }
        if (tags) {
            query = query.concat(HQL_WHERE_TAGS);
        }
        return query;
    }

    /**
     * Builds the Query for search by search criteria
     * @param searchDTO Search Criteria object
     * @return query line
     */
    public String buildNewsSearchQuery(SearchDTO searchDTO) {
        String query = HQL_PAGINATION_START;
        query = query.concat(buildSearchCriteriaQuery(searchDTO));
        query = query.concat(HQL_SEARCH_QUERY_END);
        return query;
    }

    /**
     * Builds query for counting of search results
     * @param searchDTO Search Criteria object
     * @return String query line
     */

    public String buildNewsCountQuery(SearchDTO searchDTO) {
        String query = HQL_QUANTITY_START;
        query = query.concat(buildSearchCriteriaQuery(searchDTO));
        //query = query.concat(HQL_SEARCH_QUERY_END);
        return query;
    }

    public String buildRownumByIdQuery (SearchDTO searchDTO) {
        String query = HQL_CURRENT_NEWS_ROWNUM_START;
        query = query.concat(buildSearchCriteriaQuery(searchDTO));
        query = query.concat(HQL_SEARCH_QUERY_END);
        return query;
    }
/*
    *//**
     * Builds the query for Next and Previous page search
     * @param searchDTO Search criteria object
     * @return String query
     *//*
    public String buildPrevNextQuery (SearchDTO searchDTO) {
        String query = SQL_CURRENT_NEWS_ROWNUM;
        query = query.concat(buildSearchCriteriaQuery(searchDTO));
        query = query.concat(SQL_NEXT_PREV_END);
        return query;
    }*/
}
