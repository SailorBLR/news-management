package com.epam.hubarevich.dao.impl.eclipselink;

import com.epam.hubarevich.dao.TagDAO;
import com.epam.hubarevich.dao.exception.DAOException;
import com.epam.hubarevich.dao.util.DeleteByIDUtil;
import com.epam.hubarevich.domain.Tag;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;

import javax.persistence.Query;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import java.util.List;

@Profile("elink")
@Repository
public class TagDaoImpl implements TagDAO {

    private final String JPA_FROM_TAGS =
            "SELECT T " +
            "FROM Tag T";
    private final String JPA_FROM_TAGS_BY_ID =
            "SELECT T " +
            "FROM Tag T " +
            "WHERE T.tagId=:tagId";
    private final String HQL_FIND_TAG_BY_NEWS_ID =
            "SELECT T " +
            "FROM Tag T " +
            "JOIN T.news NT " +
            "WHERE NT.newsId= :newsId";
    private final String TAG_ID = "tagId";
    private final String NEWS_ID = "newsId";
    private final String UNCHECKED = "unchecked";

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    @SuppressWarnings(UNCHECKED)
    public List<Tag> getTagsByNewsId(Long newsId) throws DAOException {
        List<Tag> tags;
        try {
            Query query = entityManager.createQuery(HQL_FIND_TAG_BY_NEWS_ID);
            query.setParameter(NEWS_ID, newsId);
            tags = query.getResultList();
        } catch (PersistenceException e) {
            throw new DAOException(e);
        }
        return tags.size() == 0 ? null : tags;
    }

    @Override
    @Deprecated
    public void unwireTagsNewsByNews(Long newsId) throws DAOException {

    }

    @Override
    @Deprecated
    public void unwireTagsNewsByTags(Long tagId) throws DAOException {

    }

    @Override
    @SuppressWarnings(UNCHECKED)
    public List<Tag> findAll() throws DAOException {
        List<Tag> tags;
        try {
            tags = entityManager.createQuery(JPA_FROM_TAGS).getResultList();
        }catch (PersistenceException e) {
            throw new DAOException(e);
        }
        return tags;
    }

    @Override
    @SuppressWarnings(UNCHECKED)
    public Tag findDomainById(Long id) throws DAOException {
        Query query;
        try {
            query = entityManager.createQuery(JPA_FROM_TAGS_BY_ID);
            query.setParameter(TAG_ID, id);
        } catch (PersistenceException e) {
            throw new DAOException(e);
        }
        return (Tag) query.getSingleResult();
    }

    @Override
    public void delete(Long id) throws DAOException {
        try {
            entityManager.remove(entityManager.find(Tag.class, id));
        } catch (PersistenceException e) {
            throw new DAOException(e);
        }
    }


    @Override
    public Long create(Tag domain) throws DAOException {
        try {
            entityManager.persist(domain);
        } catch (PersistenceException e) {
            throw new DAOException(e);
        }
        return domain.getTagId();
    }

    @Override
    public void update(Tag domain) throws DAOException {
        try {
            Tag tag = entityManager.find(Tag.class, domain.getTagId());
            tag.setTagName(domain.getTagName());
            entityManager.flush();
        } catch (PersistenceException e) {
            throw new DAOException(e);
        }
    }
}
