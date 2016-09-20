package com.epam.hubarevich.dao.impl.eclipselink;

import com.epam.hubarevich.dao.AuthorDAO;
import com.epam.hubarevich.dao.exception.DAOException;
import com.epam.hubarevich.domain.Author;

import javax.persistence.Query;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import java.util.List;

@Profile("elink")
@Repository
public class AuthorDaoImpl implements AuthorDAO {

    private final String A_NAME = "authorName";
    private final String A_ID = "authorId";
    private final String N_ID = "newsId";
    private final String UNCHECKED = "unchecked";
    private final String JPA_FIND_AUTHOR_BY_NEWS_ID =
            "SELECT A " +
            "FROM Author A " +
            "JOIN A.news NA " +
            "WHERE NA.newsId= :newsId";
    private final String JPA_FIND_AVAILABLE_AUTHOR =
            "SELECT A " +
            "FROM Author A " +
            "WHERE A.expired=null";
    private final String JPA_FIND_BY_NAME =
            "SELECT A " +
            "FROM Author A  " +
            "WHERE A.authorName = :authorName";
    private final String JPA_FIND_BY_ID =
            "SELECT A " +
            "FROM Author A  " +
            "WHERE A.authorId = :authorId";
    private final String JPA_FROM_AUTHOR =
            "SELECT A " +
            "FROM Author A";
    @PersistenceContext
    private EntityManager entityManager;


    @Override
    @SuppressWarnings(UNCHECKED)
    public Author findAuthorByName(String authorName) throws DAOException {
        List<Author> authors;
        try {
            Query query = entityManager
                    .createQuery(JPA_FIND_BY_NAME);
            query.setParameter(A_NAME, authorName);
            authors = query.getResultList();
        } catch (PersistenceException e) {
            throw new DAOException(e);
        }
        return authors.size() == 0 ? null : authors.get(0);
    }


    @Override
    @SuppressWarnings(UNCHECKED)
    public Author findAuthorByNewsId(Long newsId) throws DAOException {
        List<Author> authors;
        try {
            Query query = entityManager
                    .createQuery(JPA_FIND_AUTHOR_BY_NEWS_ID);
            query.setParameter(N_ID, newsId);
            authors = query.getResultList();
        } catch (PersistenceException e) {
            throw new DAOException(e);
        }
        return authors.size() == 0 ? null : authors.get(0);
    }

    @Override
    @Deprecated
    public void unwireNewsAuthors(Long newsId) throws DAOException {

    }

    @Override
    @SuppressWarnings(UNCHECKED)
    public List<Author> getAvailableAuthors() throws DAOException {
        List<Author> authors;
        try {
            Query query = entityManager.createQuery(JPA_FIND_AVAILABLE_AUTHOR);
            authors = query.getResultList();
        } catch (PersistenceException e) {
            throw new DAOException(e);
        }
        return authors.size() == 0 ? null : authors;
    }

    @Override
    @SuppressWarnings(UNCHECKED)
    public List<Author> findAll() throws DAOException {
        List<Author> authors;
        try {
            authors = entityManager.createQuery(JPA_FROM_AUTHOR, Author.class)
                    .getResultList();
        } catch (PersistenceException e) {
            throw new DAOException(e);
        }
        return authors.size() == 0 ? null : authors;
    }

    @Override
    @SuppressWarnings(UNCHECKED)
    public Author findDomainById(Long id) throws DAOException {
        List<Author> authors;
        try {
            Query query = entityManager
                    .createQuery(JPA_FIND_BY_ID);
            query.setParameter(A_ID, id);
            authors = query.getResultList();
        } catch (PersistenceException e) {
            throw new DAOException(e);
        }
        return authors.size() == 0 ? null : authors.get(0);
    }

    @Override
    public void delete(Long id) throws DAOException {
        Author author;
        try {
            author = entityManager.find(Author.class, id);
            entityManager.remove(author);
        } catch (PersistenceException e) {
            throw new DAOException(e);
        }
    }

    @Override
    public Long create(Author domain) throws DAOException {
        try {
            entityManager.persist(domain);
        } catch (PersistenceException e) {
            throw new DAOException(e);
        }
        return domain.getAuthorId();
    }

    @Override
    public void update(Author domain) throws DAOException {

        try {
            Author author = entityManager.find(Author.class, domain.getAuthorId());
            author.setAuthorName(domain.getAuthorName());
            author.setExpired(domain.getExpired());
            entityManager.flush();
        }catch (PersistenceException e) {
            throw new DAOException(e);
        }
    }
}
