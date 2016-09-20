package com.epam.hubarevich.dao.impl.hibernate;

import com.epam.hubarevich.dao.AuthorDAO;
import com.epam.hubarevich.dao.exception.DAOException;
import com.epam.hubarevich.domain.Author;
import com.epam.hubarevich.dao.util.DeleteByIDUtil;
import org.hibernate.*;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Profile("hibernate")
public class AuthorDaoImpl implements AuthorDAO {

    private SessionFactory sessionFactory;
    private final String A_NAME = "authorName";
    private final String A_ID = "authorId";
    private final String N_ID = "newsId";
    private final String UNCHECKED = "unchecked";
    private final String HQL_FIND_AUTHOR_BY_NEWS_ID =
            "SELECT A " +
            "FROM Author A " +
            "JOIN A.news NA " +
            "WHERE NA.newsId= :newsId";
    private final String HQL_FIND_AVAILABLE_AUTHOR =
            "SELECT A " +
            "FROM Author A " +
            "WHERE A.expired=null";
    private final String HQL_FROM_AUTHOR =
            "FROM Author";

    public AuthorDaoImpl() {
    }

    public AuthorDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    @SuppressWarnings(UNCHECKED)
    public Author findAuthorByName(String authorName) throws DAOException {
        List<Author> list;
        try {
            Criteria cr = sessionFactory.getCurrentSession().createCriteria(Author.class);
            cr.add(Restrictions.eq(A_NAME, authorName));
            list = cr.list();
        } catch (HibernateException e) {
            throw new DAOException(e);
        }
        return list.size() == 0 ? null : list.get(0);
    }


    @Override
    @SuppressWarnings(UNCHECKED)
    public Author findAuthorByNewsId(Long newsId) throws DAOException {
        List<Author> authors;
        try {
            Query query = sessionFactory.getCurrentSession().createQuery(HQL_FIND_AUTHOR_BY_NEWS_ID);
            query.setLong(N_ID, newsId);
            authors = query.list();
        } catch (HibernateException e) {
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
            Query query = sessionFactory.getCurrentSession().createQuery(HQL_FIND_AVAILABLE_AUTHOR);
            authors = query.list();
        } catch (HibernateException e) {
            throw new DAOException(e);
        }
        return authors.size() == 0 ? null : authors;
    }

    @Override
    @SuppressWarnings(UNCHECKED)
    public List<Author> findAll() throws DAOException {
        List<Author> authors;
        try {
            authors = (List) sessionFactory.getCurrentSession().createQuery(HQL_FROM_AUTHOR).list();
        } catch (HibernateException e) {
            throw new DAOException(e);
        }
        return authors.size() == 0 ? null : authors;
    }

    @Override
    @SuppressWarnings(UNCHECKED)
    public Author findDomainById(Long id) throws DAOException {
        List<Author> list;
        try {
            Criteria cr = sessionFactory.getCurrentSession().createCriteria(Author.class);
            cr.add(Restrictions.eq(A_ID, id));
            list = cr.list();
        } catch (HibernateException e) {
            throw new DAOException(e);
        }
        return list.size() == 0 ? null : list.get(0);
    }

    @Transactional
    @Override
    public void delete(Long id) throws DAOException {
        Author author = new Author();
        author.setAuthorId(id);
        try {
            DeleteByIDUtil.deleteById(Author.class, id, sessionFactory.getCurrentSession());
        }catch (HibernateException e) {
            throw new DAOException(e);
        }
    }

    @Transactional
    @Override
    public Long create(Author domain) throws DAOException {
        Long id;
        try {
            id = (Long) sessionFactory.getCurrentSession().save(domain);
        } catch (HibernateException e) {
            throw new DAOException(e);
        }
        return id;
    }

    @Override
    public void update(Author domain) throws DAOException {

        try {
            Author author = (Author) sessionFactory.getCurrentSession().load(Author.class, domain.getAuthorId());
            author.setAuthorName(domain.getAuthorName());
            author.setExpired(domain.getExpired());
            sessionFactory.getCurrentSession().flush();
        }catch (HibernateException e) {
            throw new DAOException(e);
        }
    }
}
