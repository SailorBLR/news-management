package com.epam.hubarevich.dao.impl;

import com.epam.hubarevich.dao.NewsDAO;
import com.epam.hubarevich.dao.exception.DAOException;
import com.epam.hubarevich.dao.util.HQLQueryBuilderUtil;
import com.epam.hubarevich.domain.Author;
import com.epam.hubarevich.domain.News;
import com.epam.hubarevich.domain.Tag;
import com.epam.hubarevich.domain.dto.SearchDTO;
import com.epam.hubarevich.dao.util.DeleteByIDUtil;
import com.epam.hubarevich.utils.ConfigurationManager;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class NewsDaoImpl implements NewsDAO {

    private SessionFactory sessionFactory;
    private final String HQL_FIND_NEWS_BY_AUTHOR_ID = "SELECT N FROM Author A join A.news N WHERE A.authorId= :authorId";
    private final String HQL_NEWS_BY_TAGS = "SELECT distinct N FROM News N JOIN N.tags NT WHERE NT IN (:tags)";
    private final String HQL_FIND_ALL = "FROM News";
    private final String UNCHECKED = "unchecked";
    private final String A_ID = "authorId";
    private final String N_ID = "newsId";
    private final String N_TITLE = "title";

    @Autowired
    HQLQueryBuilderUtil queryBuilderUtil;


    public NewsDaoImpl() {
    }

    public NewsDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void addNewsAuthor(Long newsId, Long authorId) throws DAOException {

        try {
            Author author = (Author) sessionFactory.getCurrentSession().load(Author.class, authorId);
            News news = (News) sessionFactory.getCurrentSession().load(News.class, newsId);
            if (author != null && news != null) {
                Set<News> newsList = new HashSet<>();
                newsList.add(news);
                author.setNews(newsList);
                sessionFactory.getCurrentSession().flush();
            }
        } catch (HibernateException e) {
            throw new DAOException(e);
        }

    }

    @Override
    @Deprecated
    public List<News> getMostCommentedNews(int newsQuantity) throws DAOException {
        return null;
    }

    @Override
    public void addTagsNews(Long newsId, List<Tag> tags) throws DAOException {

        try {
            News persistantObject = (News) sessionFactory.getCurrentSession().load(News.class, newsId);
            if (persistantObject != null) {
                Set<Tag> tagsSet = new HashSet<>();
                for (Tag tag : tags) {
                    Tag tag_found = (Tag) sessionFactory.getCurrentSession().load(Tag.class, tag.getTagId());
                    if (tag_found != null) {
                        tagsSet.add(tag_found);
                    }
                }
                persistantObject.setTags(tagsSet);
                sessionFactory.getCurrentSession().flush();
            }
        } catch (HibernateException e) {
            throw new DAOException(e);
        }
    }

    @Override
    @SuppressWarnings(UNCHECKED)
    public List<News> findNewsByAuthor(Author author) throws DAOException {
        List<News> news;
        try {
            Query query = sessionFactory.getCurrentSession().createQuery(HQL_FIND_NEWS_BY_AUTHOR_ID);
            query.setLong(A_ID, author.getAuthorId());
            news = query.list();
        } catch (HibernateException e){
            throw new DAOException(e);
        }
        return news.size() == 0 ? null : news;
    }

    @Override
    @SuppressWarnings(UNCHECKED)
    public List<News> findNewsByTags(List<Tag> tags) throws DAOException {
        List<News> news;
        try {
            Query query = sessionFactory.getCurrentSession().createQuery(HQL_NEWS_BY_TAGS);
            query.setParameterList("tags", tags);
            news = query.list();
        } catch (HibernateException e) {
            throw new DAOException(e);
        }
        return news;
    }

    @Override
    public int getTotalNewsQuantity(SearchDTO searchDTO) throws DAOException {

        Integer quantity;
        try {
            Query query = sessionFactory.getCurrentSession()
                    .createQuery(queryBuilderUtil.buildNewsCountQuery(searchDTO));
            if (searchDTO.getAuthor() != null) {
                query.setParameter("author", searchDTO.getAuthor());
            }
            if (searchDTO.getTags() != null) {
                query.setParameterList("tags", searchDTO.getTags());
            }
            quantity = Integer.parseInt(query.uniqueResult().toString());
        } catch (HibernateException e) {
            throw new DAOException(e);
        }
        return quantity;
    }

    @Override
    @SuppressWarnings(UNCHECKED)
    public List<News> getPaginatedListBySearchCriteria(SearchDTO searchDTO, int startIndex, int finishIndex) throws DAOException {
        List<News> news = new ArrayList<>();
        try {
            Query query = sessionFactory.getCurrentSession()
                    .createQuery(queryBuilderUtil.buildNewsSearchQuery(searchDTO));
            if (searchDTO.getAuthor() != null) {
                query.setParameter("author", searchDTO.getAuthor());
            }
            if (searchDTO.getTags() != null) {
                query.setParameterList("tags", searchDTO.getTags());
            }
            query.setFirstResult(startIndex - 1);
            query.setMaxResults(Integer.valueOf(ConfigurationManager.getProperty("cfg.news")));
            news.addAll(query.list());
        } catch (HibernateException e) {
            throw new DAOException(e);
        }
        return news;
    }

    @Override
    @SuppressWarnings(UNCHECKED)
    public void getPrevNextIds(SearchDTO searchDTO, Long newsId) throws DAOException {
        List<Long> ids;
        try {
            Query query = sessionFactory.getCurrentSession()
                    .createQuery(queryBuilderUtil.buildRownumByIdQuery(searchDTO));

            if (searchDTO.getAuthor() != null) {
                query.setParameter("author", searchDTO.getAuthor());
            }
            if (searchDTO.getTags() != null) {
                query.setParameterList("tags", searchDTO.getTags());
            }
            ids = query.list();
        } catch (HibernateException e) {
            throw new DAOException(e);
        }
        calculateIds(searchDTO, ids, newsId);
    }

    @Override
    @SuppressWarnings(UNCHECKED)
    public News getNewsByNewsTitle(News news) throws DAOException {
        List<News> list;
        try {
            Criteria cr = sessionFactory.getCurrentSession().createCriteria(News.class);
            cr.add(Restrictions.eq(N_TITLE, news.getTitle()));
            list=cr.list();
        } catch (HibernateException e) {
            throw new DAOException(e);
        }
        return list.size() == 0 ? null : list.get(0);
    }

    @Override
    @SuppressWarnings(UNCHECKED)
    public List<News> findAll() throws DAOException {
        List<News> news;
        try {
            news = (List) sessionFactory.getCurrentSession().createQuery(HQL_FIND_ALL).list();
        } catch (HibernateException e) {
            throw new DAOException(e);
        }
        return news.size() == 0 ? null : news;
    }

    @Override
    @SuppressWarnings(UNCHECKED)
    public News findDomainById(Long id) throws DAOException {
        List<News> list;
        try {
            Criteria cr = sessionFactory.getCurrentSession().createCriteria(News.class);
            cr.add(Restrictions.eq(N_ID, id));
            list = cr.list();
        } catch (HibernateException e) {
            throw new DAOException(e);
        }
        return list.size() == 0 ? null : list.get(0);
    }

    @Override
    public void delete(Long id) throws DAOException {
        try {
            DeleteByIDUtil.deleteById(News.class, id, sessionFactory.getCurrentSession());
        } catch (HibernateException e) {
            throw new DAOException(e);
        }
    }

    @Override
    @Transactional
    public Long create(News domain) throws DAOException {
        Long generatedId;
        try {
            generatedId = (Long) sessionFactory.getCurrentSession().save(domain);
        } catch (HibernateException e) {
            throw new DAOException(e);
        }
        return generatedId>0 ? generatedId : null;
    }

    @Override
    public void update(News domain) throws DAOException {
        try {
            News news = (News) sessionFactory.getCurrentSession().load(News.class, domain.getNewsId());
            if (news != null) {
                news = domain;
                sessionFactory.getCurrentSession().flush();
            }
        } catch (HibernateException e) {
            throw new DAOException(e);
        }
    }

    private void calculateIds(SearchDTO searchDto, List<Long> ids, Long newsId) {
        for (int i = 0; i < ids.size(); i++) {
            if (newsId.equals(ids.get(i))
                    && i != 0
                    & i != (ids.size() - 1)) {
                searchDto.setPrevId(ids.get(i - 1));
                searchDto.setNextId(ids.get(i + 1));
            }
            if (newsId.equals(ids.get(i))
                    && i == 0) {
                searchDto.setPrevId(null);
                searchDto.setNextId(ids.get(i+1));
            }
            if (newsId.equals(ids.get(i))
                    && i == (ids.size()-1)) {
                searchDto.setPrevId(ids.get(i-1));
                searchDto.setNextId(null);
            }
        }
    }
}
