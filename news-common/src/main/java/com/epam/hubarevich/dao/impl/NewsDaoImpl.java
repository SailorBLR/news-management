package com.epam.hubarevich.dao.impl;

import com.epam.hubarevich.dao.NewsDAO;
import com.epam.hubarevich.dao.exception.DAOException;
import com.epam.hubarevich.domain.Author;
import com.epam.hubarevich.domain.News;
import com.epam.hubarevich.domain.Tag;
import com.epam.hubarevich.domain.dto.SearchDTO;
import com.epam.hubarevich.utils.DeleteByIDUtil;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class NewsDaoImpl implements NewsDAO {

    private SessionFactory sessionFactory;
    private final String HQL_FIND_NEWS_BY_AUTHOR_ID = "SELECT N FROM Author A join A.news N WHERE A.authorId= :authorId";
    private final String HQL_FIND_ALL = "FROM News";
    private final String UNCHECKED = "unchecked";
    private final String A_ID = "authorId";
    private final String N_ID = "newsId";
    private final String N_TITLE = "title";


    public NewsDaoImpl(){}

    public NewsDaoImpl(SessionFactory sessionFactory) {
        this.sessionFactory=sessionFactory;
    }

    @Transactional
    @Override
    public void addNewsAuthor(Long newsId, Long authorId) throws DAOException {

        Author author = (Author)sessionFactory.getCurrentSession().load(Author.class,authorId);
        News news = (News) sessionFactory.getCurrentSession().load(News.class,newsId);
        if (author!=null&&news!=null) {
            Set<News> newsList = new HashSet<>();
            newsList.add(news);
            author.setNews(newsList);
            sessionFactory.getCurrentSession().flush();
        }

    }

    @Override
    public List<News> getMostCommentedNews(int newsQuantity) throws DAOException {
        return null;
    }

    @Override
    public void addTagsNews(Long newsId, List<Tag> tags) throws DAOException {
        News persistantObject = (News) sessionFactory.getCurrentSession().load(News.class,newsId);
        if(persistantObject!=null) {
            Set<Tag> tagsSet = new HashSet<>();
            for (Tag tag : tags) {
                Tag tag_found = (Tag) sessionFactory.getCurrentSession().load(Tag.class,tag.getTagId());
                if(tag_found!=null) {
                    tagsSet.add(tag_found);
                }
            }
            persistantObject.setTags(tagsSet);
            sessionFactory.getCurrentSession().flush();
        }
    }

    @Override
    @SuppressWarnings(UNCHECKED)
    public List<News> findNewsByAuthor(Author author) throws DAOException {
        Query query = sessionFactory.getCurrentSession().createQuery(HQL_FIND_NEWS_BY_AUTHOR_ID);
        query.setLong(A_ID, author.getAuthorId());
        List<News> news = query.list();
        return news.size() == 0 ? null : news;
    }

    @Override
    public List<News> findNewsByTags(List<Tag> tags) throws DAOException {
        return null;
    }

    @Override
    public int getTotalNewsQuantity(SearchDTO searchDTO) throws DAOException {
        return 0;
    }

    @Override
    public List<News> getPaginatedListBySearchCriteria(SearchDTO searchDTO, int startIndex, int finishIndex) throws DAOException {
        return null;
    }

    @Override
    public void getPrevNextIds(SearchDTO searchDTO, Long newsId) throws DAOException {

    }

    @Override
    @SuppressWarnings(UNCHECKED)
    public News getNewsByNewsTitle(News news) throws DAOException {
        Criteria cr = sessionFactory.getCurrentSession().createCriteria(News.class);
        cr.add(Restrictions.eq(N_TITLE, news.getTitle()));
        List<News> list = cr.list();
        return list.size() == 0 ? null : list.get(0);
    }

    @Override
    @SuppressWarnings(UNCHECKED)
    public List<News> findAll() throws DAOException {
        return (List) sessionFactory.getCurrentSession().createQuery(HQL_FIND_ALL).list();
    }

    @Override
    @SuppressWarnings(UNCHECKED)
    public News findDomainById(Long id) throws DAOException {
        Criteria cr = sessionFactory.getCurrentSession().createCriteria(News.class);
        cr.add(Restrictions.eq(N_ID, id));
        List<News> list = cr.list();
        return list.size() == 0 ? null : list.get(0);
    }

    @Override
    public void delete(Long id) throws DAOException {
        DeleteByIDUtil.deleteById(News.class,id,sessionFactory.getCurrentSession());
    }

    @Override
    @Transactional
    public Long create(News domain) throws DAOException {
        return (Long) sessionFactory.getCurrentSession().save(domain);
    }

    @Override
    public void update(News domain) throws DAOException {
        News news = (News) sessionFactory.getCurrentSession().load(News.class,domain.getNewsId());
        if(news!=null){
            news = domain;
            sessionFactory.getCurrentSession().flush();
        }
    }
}
