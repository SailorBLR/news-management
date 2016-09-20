package com.epam.hubarevich.dao.impl.eclipselink;

import com.epam.hubarevich.dao.NewsDAO;
import com.epam.hubarevich.dao.exception.DAOException;
import com.epam.hubarevich.dao.util.DeleteByIDUtil;
import com.epam.hubarevich.dao.util.HQLQueryBuilderUtil;
import com.epam.hubarevich.domain.Author;
import com.epam.hubarevich.domain.News;
import com.epam.hubarevich.domain.Tag;
import com.epam.hubarevich.domain.dto.SearchDTO;
import com.epam.hubarevich.utils.ConfigurationManager;
import org.hibernate.HibernateException;

import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Profile("elink")
@Repository
public class NewsDaoImpl implements NewsDAO {

    private final String JPA_FIND_NEWS_BY_AUTHOR_ID =
            "SELECT N " +
                    "FROM News N " +
                    "JOIN N.authors NA " +
                    "WHERE NA.authorId= :authorId";
    private final String JPA_NEWS_BY_TAGS =
            "SELECT DISTINCT N " +
                    "FROM News N " +
                    "JOIN N.tags NT " +
                    "WHERE NT IN :tags";
    private final String JPA_FIND_BY_ID =
            "SELECT N " +
                    "FROM News N  " +
                    "WHERE N.newsId = :newsId";
    private final String JPA_FROM_NEWS =
            "SELECT N " +
                    "FROM News N";
    private final String JPA_FIND_BY_TITLE =
            "SELECT N " +
                    "FROM News N " +
                    "WHERE N.title = :title";
    private final String UNCHECKED = "unchecked";
    private final String A_ID = "authorId";
    private final String N_ID = "newsId";
    private final String N_TITLE = "title";
    private final String TAGS = "tags";
    private final String AUTHOR = "author";

    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    HQLQueryBuilderUtil queryBuilderUtil;


    @Override
    public void addNewsAuthor(Long newsId, Long authorId) throws DAOException {

        try {
            Author author = entityManager.find(Author.class, authorId);
            News news = entityManager.find(News.class, newsId);

            if (author != null && news != null) {
                Set<Author> authorsList = new HashSet<>();
                authorsList.add(author);
                news.setAuthors(authorsList);
                entityManager.persist(news);
                entityManager.persist(author);
                entityManager.flush();
            }
        } catch (PersistenceException e) {
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
            News news = entityManager.find(News.class, newsId);
            if (news != null) {
                Set<Tag> tagsSet = new HashSet<>();
                for (Tag tag : tags) {
                    Tag tag_found = entityManager.find(Tag.class, tag.getTagId());
                    if (tag_found != null) {
                        tagsSet.add(tag_found);
                    }
                }
                news.setTags(tagsSet);
                entityManager.persist(news);
                entityManager.flush();
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
            Query query = entityManager.createQuery(JPA_FIND_NEWS_BY_AUTHOR_ID);
            query.setParameter(A_ID, author.getAuthorId());
            news = query.getResultList();
        } catch (PersistenceException e) {
            throw new DAOException(e);
        }
        return news.size() == 0 ? null : news;
    }

    @Override
    @SuppressWarnings(UNCHECKED)
    public List<News> findNewsByTags(List<Tag> tags) throws DAOException {
        List<News> news;
        try {
            Query query = entityManager.createQuery(JPA_NEWS_BY_TAGS);
            query.setParameter(TAGS, tags);
            news = query.getResultList();
        } catch (HibernateException e) {
            throw new DAOException(e);
        }
        return news.size() == 0 ? null : news;
    }

    @Override
    public int getTotalNewsQuantity(SearchDTO searchDTO) throws DAOException {

        Integer quantity;
        try {
            Query query = entityManager
                    .createQuery(queryBuilderUtil.buildNewsCountQuery(searchDTO));
            if (searchDTO.getAuthor() != null) {
                query.setParameter(AUTHOR, searchDTO.getAuthor());
            }
            if (searchDTO.getTags() != null) {
                query.setParameter(TAGS, searchDTO.getTags());
            }
            quantity = ((Long) query.getSingleResult()).intValue();


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
            Query query = entityManager.createQuery(queryBuilderUtil.buildNewsSearchQuery(searchDTO));

            if (searchDTO.getTags() != null) {
                query.setParameter(TAGS, searchDTO.getTags());
            }
            if (searchDTO.getAuthor() != null) {
                query.setParameter(AUTHOR, searchDTO.getAuthor());
            }
            query.setFirstResult(startIndex - 1);
            query.setMaxResults(Integer.valueOf(ConfigurationManager.getProperty("cfg.news")));
            news.addAll(query.getResultList());
        } catch (HibernateException e) {
            throw new DAOException(e);
        }
        return news.size() == 0 ? null : news;
    }

    @Override
    @SuppressWarnings(UNCHECKED)
    public void getPrevNextIds(SearchDTO searchDTO, Long newsId) throws DAOException {
       List<Long> ids;
        try {
            Query query = entityManager
                    .createQuery(queryBuilderUtil.buildRownumByIdQuery(searchDTO));

            if (searchDTO.getAuthor() != null) {
                query.setParameter(AUTHOR, searchDTO.getAuthor());
            }
            if (searchDTO.getTags() != null) {
                query.setParameter(TAGS, searchDTO.getTags());
            }
            ids = query.getResultList();
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
            Query query = entityManager.createQuery(JPA_FIND_BY_TITLE);
            list = query.setParameter(N_TITLE, news.getTitle()).getResultList();
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
            news = entityManager.createQuery(JPA_FROM_NEWS, News.class)
                    .getResultList();
        } catch (PersistenceException e) {
            throw new DAOException(e);
        }
        return news.size() == 0 ? null : news;
    }

    @Override
    @SuppressWarnings(UNCHECKED)
    public News findDomainById(Long id) throws DAOException {
        List<News> news;
        try {
            javax.persistence.Query query = entityManager
                    .createQuery(JPA_FIND_BY_ID);
            query.setParameter(N_ID, id);
            news = query.getResultList();
        } catch (PersistenceException e) {
            throw new DAOException(e);
        }

        return news.size() == 0 ? null : news.get(0);
    }

    @Override
    public void delete(Long id) throws DAOException {
        try {
            News news = entityManager.find(News.class, id);
            entityManager.remove(news);
        } catch (PersistenceException e) {
            throw new DAOException(e);
        }
    }

    @Override
    public Long create(News domain) throws DAOException {
        try {
            entityManager.persist(domain);
        } catch (PersistenceException e) {
            throw new DAOException(e);
        }
        return domain.getNewsId() > 0 ? domain.getNewsId() : null;
    }

    @Override
    public void update(News domain) throws DAOException {
        try {
            News news = entityManager.find(News.class, domain.getNewsId());
            news = domain;
            entityManager.flush();
        } catch (PersistenceException e) {
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
                searchDto.setNextId(ids.get(i + 1));
            }
            if (newsId.equals(ids.get(i))
                    && i == (ids.size() - 1)) {
                searchDto.setPrevId(ids.get(i - 1));
                searchDto.setNextId(null);
            }
        }
    }
}
