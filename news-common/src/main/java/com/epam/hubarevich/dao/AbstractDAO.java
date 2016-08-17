package com.epam.hubarevich.dao;

import com.epam.hubarevich.dao.exception.DAOException;
import com.epam.hubarevich.domain.Domain;

import java.util.List;

/**
 * Interface for common CRUD operations
 * @param <T> Class-entity extends Domain
 */
public interface AbstractDAO <T extends Domain> {
    /**
     * @return List of all Objects Domain from database
     * @throws DAOException if SQLException thrown
     */
    List<T> findAll() throws DAOException;

    /**
     * finds the Domain by it's id
     * @param id unique Domain identification number
     * @return Domain object
     * @throws DAOException if SQLException thrown
     */
    T findDomainById(Long id) throws DAOException;

    /**
     * deletes Domain by it's ID
     * @param id unique Domain identification number
     * @throws DAOException if SQLException thrown
     */
    void delete(Long id) throws DAOException;

    /**
     * deletes the field in DB by Object class Domain
     * @param domain Object class T extends Domain
     * @return true if the deleting is successfully
     * @throws DAOException if SQLException thrown
     */

    Long create(T domain) throws DAOException;

    /**
     * updates a field in DB by Object class extends Domain
     * @param domain Object class T extends Domain
     * @throws DAOException if SQLException thrown
     */
    void update(T domain) throws DAOException;

    /**
     * finds a field in DB by Object class extends Domain
     * @param domain Object class T extends Domain
     * @return Domain object
     * @throws DAOException if SQLException thrown
     */
}
