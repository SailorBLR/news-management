package com.epam.hubarevich.dao.impl;

import com.epam.hubarevich.dao.AuthorDAO;
import com.epam.hubarevich.dao.exception.DAOException;
import com.epam.hubarevich.domain.Author;
import com.epam.hubarevich.utils.DeleteByIDUtil;
import org.hibernate.*;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;


public class AuthorDaoImpl implements AuthorDAO {

    private SessionFactory sessionFactory;
    private final String A_NAME = "authorName";
    private final String A_ID = "authorId";
    private final String N_ID = "newsId";
    private final String UNCHECKED = "unchecked";
    private final String HQL_FIND_AUTHOR_BY_NEWS_ID = "SELECT A FROM Author A join A.news NA WHERE NA.newsId= :newsId";
    private final String HQL_FIND_AVAILABLE_AUTHOR = "SELECT A FROM Author A WHERE A.expired=null";
    private final String HQL_FROM_AUTHOR = "FROM Author";

    public AuthorDaoImpl() {
    }

    public AuthorDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    @SuppressWarnings(UNCHECKED)
    public Author findAuthorByName(String authorName) throws DAOException {
        Criteria cr = sessionFactory.getCurrentSession().createCriteria(Author.class);
        cr.add(Restrictions.eq(A_NAME, authorName));
        List<Author> list = cr.list();
        return list.size() == 0 ? null : list.get(0);
    }


    @Override
    @SuppressWarnings(UNCHECKED)
    public Author findAuthorByNewsId(Long newsId) throws DAOException {
        Query query = sessionFactory.getCurrentSession().createQuery(HQL_FIND_AUTHOR_BY_NEWS_ID);
        query.setLong(N_ID, newsId);
        List<Author> authors = query.list();
        return authors.size() == 0 ? null : authors.get(0);
    }

    @Override
    public void unwireNewsAuthors(Long newsId) throws DAOException {

    }

    @Override
    @SuppressWarnings(UNCHECKED)
    public List<Author> getAvailableAuthors() throws DAOException {
        Query query = sessionFactory.getCurrentSession().createQuery(HQL_FIND_AVAILABLE_AUTHOR);
        return query.list();
    }

    @Override
    @SuppressWarnings(UNCHECKED)
    public List<Author> findAll() throws DAOException {

        return (List) sessionFactory.getCurrentSession().createQuery(HQL_FROM_AUTHOR).list();
    }

    @Override
    @SuppressWarnings(UNCHECKED)
    public Author findDomainById(Long id) throws DAOException {
        Criteria cr = sessionFactory.getCurrentSession().createCriteria(Author.class);
        cr.add(Restrictions.eq(A_ID, id));
        List<Author> list = cr.list();
        return list.size() == 0 ? null : list.get(0);
    }

    @Transactional
    @Override
    public void delete(Long id) throws DAOException {
        Author author = new Author();
        author.setAuthorId(id);
        DeleteByIDUtil.deleteById(Author.class, id, sessionFactory.getCurrentSession());
    }

    @Transactional
    @Override
    public Long create(Author domain) throws DAOException {
        return (Long) sessionFactory.getCurrentSession().save(domain);
    }

    @Override
    public void update(Author domain) throws DAOException {

        Author author = (Author) sessionFactory.getCurrentSession().load(Author.class, domain.getAuthorId());
        author.setAuthorName(domain.getAuthorName());
        author.setExpired(domain.getExpired());
        sessionFactory.getCurrentSession().flush();
    }
}
