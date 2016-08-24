package com.epam.hubarevich.utils;

import com.epam.hubarevich.domain.Tag;
import com.epam.hubarevich.domain.dto.SearchDTO;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by Anton_Hubarevich on 7/25/2016.
 */
@Component
public class StatementFillerUtil {
    /**
     * Fills the blanks in prepared statement for Search criteria based search
     * @param preparedStatement initialised Prepared Statement
     * @param searchDTO Search criteria object
     * @param startIndex start index of list
     * @param finishIndex end index of list
     * @throws SQLException
     */
    public void statementPreparerSearch(PreparedStatement preparedStatement,
                                        SearchDTO searchDTO, int startIndex, int finishIndex) throws SQLException {
        int searchCode = getOperationCode(searchDTO);
        switch (searchCode) {
            case 1:
                preparedStatement.setLong(1, searchDTO.getAuthor().getAuthorId());
                preparedStatement.setInt(3, startIndex);
                preparedStatement.setInt(2, finishIndex);
                break;
            case 2:
                preparedStatement.setLong(1, searchDTO.getAuthor().getAuthorId());
                int i = 2;
                for (Tag tag : searchDTO.getTags()) {
                    preparedStatement.setLong(i, tag.getTagId());
                    i++;
                }
                preparedStatement.setInt(i, finishIndex);
                preparedStatement.setInt(i + 1, startIndex);
                break;
            case 3:
                int j = 1;
                for (Tag tag : searchDTO.getTags()) {
                    preparedStatement.setLong(j, tag.getTagId());
                    j++;
                }
                preparedStatement.setInt(j, finishIndex);
                preparedStatement.setInt(j + 1, startIndex);

                break;
            case 4:
                preparedStatement.setInt(2, startIndex);
                preparedStatement.setInt(1, finishIndex);
                break;
            default:
                break;
        }
    }

    /**
     * Fills the blanks in prepared statement of Counting the search resultSet
     *
     * @param preparedStatement initialised PreparedStatement
     * @param searchDTO         searchDTO object
     */
    public void statementPreparerCount (PreparedStatement preparedStatement,
                                        SearchDTO searchDTO) throws SQLException {
        int searchCode = getOperationCode(searchDTO);
        switch (searchCode) {
            case 1:
                preparedStatement.setLong(1, searchDTO.getAuthor().getAuthorId());
                break;
            case 2:
                preparedStatement.setLong(1, searchDTO.getAuthor().getAuthorId());
                int i = 2;
                for (Tag tag : searchDTO.getTags()) {
                    preparedStatement.setLong(i, tag.getTagId());
                    i++;
                }
                break;
            case 3:
                int j = 1;
                for (Tag tag : searchDTO.getTags()) {
                    preparedStatement.setLong(j, tag.getTagId());
                    j++;
                }
                break;
            default:
                break;
        }

    }

    /**
     * Prepares the statement to search rownumber by newsId
     * @param preparedStatement not filled prepared statement
     * @param searchDTO search criteria object
     * @param newsId Long identifier
     * @throws SQLException
     */
    public void prepareRownumFindQuery (PreparedStatement preparedStatement,
                                        SearchDTO searchDTO, Long newsId ) throws SQLException {
        int searchCode = getOperationCode(searchDTO);
        switch (searchCode) {
            case 1:
                preparedStatement.setLong(1, searchDTO.getAuthor().getAuthorId());
                preparedStatement.setLong(2,newsId);
                break;
            case 2:
                preparedStatement.setLong(1, searchDTO.getAuthor().getAuthorId());
                int i = 2;
                for (Tag tag : searchDTO.getTags()) {
                    preparedStatement.setLong(i, tag.getTagId());
                    i++;
                }
                preparedStatement.setLong(i,newsId);
                break;
            case 3:
                int j = 1;
                for (Tag tag : searchDTO.getTags()) {
                    preparedStatement.setLong(j, tag.getTagId());
                    j++;
                }
                preparedStatement.setLong(j,newsId);
                break;
            case 4:
                preparedStatement.setLong(1,newsId);

                break;
            default:
                break;
        }
    }

    /**
     * Prepares the statement to search next and previous ids
     * @param preparedStatement not filled prepared statement
     * @param searchDTO search criteria object
     * @param rowNum int rowNumber
     * @throws SQLException
     */
    public void prepareNextPrevFindQuery (PreparedStatement preparedStatement,
                                        SearchDTO searchDTO, int rowNum ) throws SQLException {
        int searchCode = getOperationCode(searchDTO);
        switch (searchCode) {
            case 1:
                preparedStatement.setLong(1, searchDTO.getAuthor().getAuthorId());
                preparedStatement.setInt(2,rowNum-1);
                preparedStatement.setInt(3,rowNum+1);

                break;
            case 2:
                preparedStatement.setLong(1, searchDTO.getAuthor().getAuthorId());
                int i = 2;
                for (Tag tag : searchDTO.getTags()) {
                    preparedStatement.setLong(i, tag.getTagId());
                    i++;
                }
                preparedStatement.setInt(i,rowNum-1);
                preparedStatement.setInt(i+1,rowNum+1);
                break;
            case 3:
                int j = 1;
                for (Tag tag : searchDTO.getTags()) {
                    preparedStatement.setLong(j, tag.getTagId());
                    j++;
                }
                preparedStatement.setInt(j,rowNum-1);
                preparedStatement.setInt(j+1,rowNum+1);
                break;
            case 4:
                preparedStatement.setInt(1,(rowNum-1));
                preparedStatement.setInt(2,(rowNum+1));
                break;
            default:
                break;
        }
    }

    /**
     * Gets the Search criteria object and decides the operation type
     * 1 - search by author
     * 2 - search by author & tags
     * 3 - search by tags
     * 4 - full list search
     * @param searchDTO Search Criteria object
     * @return Integer operation code
     */
    private int getOperationCode (SearchDTO searchDTO) {
        int searchCode = 4;
        if (searchDTO.getAuthor() != null && searchDTO.getTags() == null) {
            searchCode = 1;
        }
        if (searchDTO.getAuthor() != null && searchDTO.getTags() != null) {
            searchCode = 2;
        }
        if (searchDTO.getAuthor() == null && searchDTO.getTags() != null) {
            searchCode = 3;
        }
        return searchCode;
    }

}
