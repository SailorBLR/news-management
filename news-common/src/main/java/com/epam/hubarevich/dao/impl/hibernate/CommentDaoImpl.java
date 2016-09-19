package com.epam.hubarevich.dao.impl.hibernate;

import com.epam.hubarevich.dao.CommentDAO;
import com.epam.hubarevich.dao.exception.DAOException;
import com.epam.hubarevich.domain.Comment;
import com.epam.hubarevich.dao.util.DeleteByIDUtil;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Profile("hibernate")
public class CommentDaoImpl implements CommentDAO {

    private SessionFactory sessionFactory;

    private final String C_ID = "commentId";
    private final String N_ID = "newsId";
    private final String HQL_COMMENT_BY_NEWS_ID = "SELECT C " +
                                                  "FROM Comment C " +
                                                  "WHERE C.newsId=:newsId";

    private final String HQL_ALL_COMMENTS = "SELECT C " +
                                            "FROM Comment C";
    private final String UNCHECKED = "unchecked";


    public CommentDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    @SuppressWarnings(UNCHECKED)
    public List<Comment> findCommentsByNewsId(Long newsId) throws DAOException {
        Query query;
        try {
            query = sessionFactory.getCurrentSession().createQuery(HQL_COMMENT_BY_NEWS_ID);
            query.setLong(N_ID, newsId);
        } catch (HibernateException e) {
            throw new DAOException(e);
        }
        return query.list();
    }

    @Override
    @Deprecated
    public void deleteCommentsByNewsId(Long newsId) throws DAOException {

    }

    @Override
    @SuppressWarnings(UNCHECKED)
    public List<Comment> findAll() throws DAOException {

        Query query;
        try {
            query = sessionFactory.getCurrentSession().createQuery(HQL_ALL_COMMENTS);
        } catch (HibernateException e) {
            throw new DAOException(e);
        }
        return query.list();
    }

    @Override
    @SuppressWarnings(UNCHECKED)
    public Comment findDomainById(Long id) throws DAOException {
        List<Comment> list;
        try {
            Criteria cr = sessionFactory.getCurrentSession().createCriteria(Comment.class);
            cr.add(Restrictions.eq(C_ID, id));
            list = cr.list();
        } catch (HibernateException e) {
            throw new DAOException(e);
        }
        return list.size() == 0 ? null : list.get(0);
    }

    @Override
    public void delete(Long id) throws DAOException{
        try {
            DeleteByIDUtil.deleteById(Comment.class, id, sessionFactory.getCurrentSession());
        } catch (HibernateException e) {
            throw new DAOException(e);
        }
    }

    @Override

    public Long create(Comment domain) throws DAOException {

        Long id;
        try {
            sessionFactory.getCurrentSession().save(domain);
            sessionFactory.getCurrentSession().persist(domain);

        } catch (HibernateException e){
            throw new DAOException(e);
        }
        return domain.getCommentId();
    }

    @Override
    @Deprecated
    public void update(Comment domain) throws DAOException {


    }
}
