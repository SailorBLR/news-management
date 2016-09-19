package com.epam.hubarevich.dao.util;

import com.epam.hubarevich.domain.Tag;
import com.epam.hubarevich.domain.dto.SearchDTO;
import org.hibernate.Query;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Anton_Hubarevich on 9/2/2016.
 */
@Component
public class HQLQueryBuilderUtil {


/*    "SELECT DISTINCT N " +
            "FROM News N LEFT JOIN N.comments NC " +
            "INNER JOIN N.authors NA " +
            "INNER JOIN N.tags NT " +
            "WHERE NA = :author OR NT IN :tags " +
            "group by N.newsId,N.title,N.shortText,N.fullText,N.newsCreationDate, N.newsModificationDate " +
            "order by count (NC) desc, N.newsModificationDate DESC"*/

    private final String HQL_PAGINATION_START =
            "SELECT N " +
            "FROM News N " +
            "LEFT JOIN N.comments NC ";
    private final String HQL_JOIN_AUTHORS = "INNER JOIN N.authors NA ";
    private final String HQL_JOIN_TAGS = "INNER JOIN N.tags NT ";
    private final String HQL_WHERE = "WHERE ";
    private final String HQL_OR = "OR ";
    private final String HQL_WHERE_AUTHOR = "NA = :author ";
    private final String HQL_WHERE_TAGS = "NT IN :tags ";
    private final String HQL_SEARCH_QUERY_END =
            "group by N.newsId,N.title,N.shortText,N.fullText,N.newsCreationDate, N.newsModificationDate " +
            " order by count (NC) desc, N.newsModificationDate DESC";

    private final String HQL_QUANTITY_START =
            "SELECT COUNT (N) " +
            "FROM News N ";
    private final String HQL_ID_LIST =
            "SELECT N.newsId " +
            "FROM News N " +
            "LEFT JOIN N.comments NC ";



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
        return query;
    }

    public String buildRownumByIdQuery (SearchDTO searchDTO) {
        String query = HQL_ID_LIST;
        query = query.concat(buildSearchCriteriaQuery(searchDTO));
        query = query.concat(HQL_SEARCH_QUERY_END);
        return query;
    }
}
