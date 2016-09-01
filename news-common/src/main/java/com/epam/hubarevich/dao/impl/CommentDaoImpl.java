package com.epam.hubarevich.dao.impl;

import com.epam.hubarevich.dao.CommentDAO;
import com.epam.hubarevich.dao.exception.DAOException;
import com.epam.hubarevich.domain.Author;
import com.epam.hubarevich.domain.Comment;
import com.epam.hubarevich.utils.DeleteByIDUtil;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import java.util.List;



public class CommentDaoImpl implements CommentDAO{

    private SessionFactory sessionFactory;

    private final String C_ID = "commentId";
    private final String UNCHECKED = "unchecked";


    public CommentDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Comment> findCommentsByNewsId(Long newsId) throws DAOException {
        return null;
    }

    @Override
    public void deleteCommentsByNewsId(Long newsId) throws DAOException {

    }

    @Override
    public List<Comment> findAll() throws DAOException {
        return null;
    }

    @Override
    @SuppressWarnings(UNCHECKED)
    public Comment findDomainById(Long id) throws DAOException {
        Criteria cr = sessionFactory.getCurrentSession().createCriteria(Comment.class);
        cr.add(Restrictions.eq(C_ID, id));
        List<Comment> list = cr.list();
        return list.size() == 0 ? null : list.get(0);
    }

    @Override
    public void delete(Long id) {

        DeleteByIDUtil.deleteById(Comment.class,id,sessionFactory.getCurrentSession());

    }

    @Override
    public Long create(Comment domain) throws DAOException {
        return null;
    }

    @Override
    public void update(Comment domain) throws DAOException {

    }
}
