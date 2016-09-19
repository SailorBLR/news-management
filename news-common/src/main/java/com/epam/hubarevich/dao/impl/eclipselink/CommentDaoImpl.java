package com.epam.hubarevich.dao.impl.eclipselink;

import com.epam.hubarevich.dao.CommentDAO;
import com.epam.hubarevich.dao.exception.DAOException;
import com.epam.hubarevich.domain.Comment;
import org.hibernate.HibernateException;

import javax.persistence.PersistenceException;
import javax.persistence.Query;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Profile("elink")
@Repository
public class CommentDaoImpl implements CommentDAO {



    private final String C_ID = "commentId";
    private final String N_ID = "newsId";
    private final String JPA_COMMENT_BY_NEWS_ID =
            "SELECT C " +
            "FROM Comment C " +
            "WHERE C.newsId=:newsId";
    private final String JPA_FIND_BY_ID =
            "select C " +
            "from Comment C  " +
            "where C.commentId = :commentId";
    private final String JPA_ALL_COMMENTS =
            "SELECT C " +
            "FROM Comment C";
    private final String UNCHECKED = "unchecked";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @SuppressWarnings(UNCHECKED)
    public List<Comment> findCommentsByNewsId(Long newsId) throws DAOException {
       List<Comment> comments;
        try {
            Query query = entityManager.createQuery(JPA_COMMENT_BY_NEWS_ID);
            query.setParameter(N_ID, newsId);
            comments = query.getResultList();
        } catch (PersistenceException e) {
            throw new DAOException(e);
        }
        return comments.size()==0 ? null : comments;
    }

    @Override
    @Deprecated
    public void deleteCommentsByNewsId(Long newsId) throws DAOException {

    }

    @Override
    @SuppressWarnings(UNCHECKED)
    public List<Comment> findAll() throws DAOException {
        List<Comment> comments;
        try {
            Query query = entityManager.createQuery(JPA_ALL_COMMENTS);
            comments = query.getResultList();
        } catch (PersistenceException e) {
            throw new DAOException(e);
        }
        return comments;
    }

    @Override
    @SuppressWarnings(UNCHECKED)
    public Comment findDomainById(Long id) throws DAOException {
        List<Comment> list;
        try {
            Query query = entityManager.createQuery(JPA_FIND_BY_ID);
            query.setParameter(C_ID,id);
            list = query.getResultList();
        } catch (PersistenceException e) {
            throw new DAOException(e);
        }
        return list.size() == 0 ? null : list.get(0);
    }

    @Override
    public void delete(Long id) throws DAOException{
        Comment comment;
        try {
            comment = entityManager.find(Comment.class,id);
            entityManager.remove(comment);
        } catch (PersistenceException e) {
            throw new DAOException(e);
        }
    }

    @Override

    public Long create(Comment domain) throws DAOException {
        try {
            entityManager.persist(domain);
        } catch (PersistenceException e){
            throw new DAOException(e);
        }
        return domain.getCommentId();
    }

    @Override
    @Deprecated
    public void update(Comment domain) throws DAOException {


    }
}
