package com.epam.hubarevich.service;

import com.epam.hubarevich.domain.Author;
import com.epam.hubarevich.service.exception.LogicException;

import java.util.List;

/**
 * Class for Author Service operations
 * @author Anton_Hubarevich
 * @version 1.0
 */
public interface AuthorService {
    /**
     * Creates Author object
     * @param author Author instance
     * @return Author identifier
     * @throws LogicException if DAOException obtained
     */
    Long createAuthor(Author author) throws LogicException;

    /**
     * Deletes field from database
     * @param authorId Long value
     * @throws LogicException if DAOException obtained
     */
    void deleteAuthor(Long authorId) throws LogicException;
    void updateAuthor(Author author) throws LogicException;
    Author getAuthorByNewsId(Long newsId) throws LogicException;
    List<Author> getListOfAuthors() throws LogicException;
    void unwireNewsAuthors(Long newsId) throws LogicException;
    List<Author> getListAvailableAuthors() throws LogicException;
}
