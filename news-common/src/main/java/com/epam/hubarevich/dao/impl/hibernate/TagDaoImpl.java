package com.epam.hubarevich.dao.impl.hibernate;

import com.epam.hubarevich.dao.TagDAO;
import com.epam.hubarevich.dao.exception.DAOException;
import com.epam.hubarevich.domain.Tag;
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
public class TagDaoImpl implements TagDAO {

    private SessionFactory sessionFactory;
    private final String HQL_FROM_TAGS = "FROM Tag";
    private final String HQL_FIND_TAG_BY_NEWS_ID = "SELECT T FROM Tag T join T.news NT WHERE NT.newsId= :newsId";
    private final String TAG_ID = "tagId";
    private final String NEWS_ID = "newsId";
    private final String UNCHECKED = "unchecked";

    public TagDaoImpl(){}

    public TagDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    @SuppressWarnings(UNCHECKED)
    public List<Tag> getTagsByNewsId(Long newsId) throws DAOException {
        Query query = sessionFactory.getCurrentSession().createQuery(HQL_FIND_TAG_BY_NEWS_ID);
        query.setLong(NEWS_ID, newsId);
        List<Tag> tags = query.list();
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
        return (List) sessionFactory.getCurrentSession().createQuery(HQL_FROM_TAGS).list();
    }

    @Override
    @SuppressWarnings(UNCHECKED)
    public Tag findDomainById(Long id) throws DAOException {
        Criteria cr = sessionFactory.getCurrentSession().createCriteria(Tag.class);
        cr.add(Restrictions.eq(TAG_ID, id));
        List<Tag> list = cr.list();
        return list.size() == 0 ? null : list.get(0);
    }

    @Override
    public void delete(Long id) throws DAOException {
        try {
            DeleteByIDUtil.deleteById(Tag.class, id, sessionFactory.getCurrentSession());
        } catch (HibernateException e) {
            throw new DAOException(e);
        }
    }


    @Override
    public Long create(Tag domain) throws DAOException {
        return (Long) sessionFactory.getCurrentSession().save(domain);
    }

    @Override
    public void update(Tag domain) throws DAOException {
        Tag tag = (Tag) sessionFactory.getCurrentSession().load(Tag.class, domain.getTagId());
        tag.setTagName(domain.getTagName());
        sessionFactory.getCurrentSession().flush();
    }
}
