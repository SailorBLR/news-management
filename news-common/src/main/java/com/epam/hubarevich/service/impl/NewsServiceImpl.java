package com.epam.hubarevich.service.impl;

import com.epam.hubarevich.dao.AuthorDAO;
import com.epam.hubarevich.dao.CommentDAO;
import com.epam.hubarevich.dao.NewsDAO;
import com.epam.hubarevich.dao.TagDAO;
import com.epam.hubarevich.dao.exception.DAOException;
import com.epam.hubarevich.domain.News;
import com.epam.hubarevich.domain.dto.NewsDTO;
import com.epam.hubarevich.domain.dto.SearchDTO;
import com.epam.hubarevich.service.NewsService;
import com.epam.hubarevich.service.exception.LogicException;
import com.epam.hubarevich.utils.ConfigurationManager;
import com.epam.hubarevich.utils.NewsCheckUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;


@Service
@Transactional

public class NewsServiceImpl implements NewsService {
    private final String CFG = "cfg.news";
    private final String CFG_START = "cfg.start_index";
    private final String CFG_END = "cfg.end_index";
    private final String MESSAGE_NO_NEWS = "No such news message in the Database";


    @Autowired
    private NewsDAO newsDAO;

    @Autowired
    private AuthorDAO authorDAO;

    @Autowired
    private TagDAO tagDao;

    @Autowired
    private CommentDAO commentDAO;

    @Override

    public Long createNews(NewsDTO newsDTO) throws LogicException {
        Long successMarker = 0L;
        newsDTO.getNews().setNewsCreationDate(Calendar.getInstance().getTime());
        newsDTO.getNews().setNewsModificationDate(Calendar.getInstance().getTime());

        try {
            if (NewsCheckUtil.checkNewsDto(newsDTO) && newsDAO.getNewsByNewsTitle(newsDTO.getNews()) == null) {
                successMarker = newsDAO.create(newsDTO.getNews());
                if (successMarker != 0) {
                    newsDAO.addTagsNews(newsDTO.getNews().getNewsId(), newsDTO.getTags());
                    newsDAO.addNewsAuthor(newsDTO.getNews().getNewsId(), newsDTO.getAuthor().getAuthorId());
                }
            }
        } catch (DAOException e) {
            throw new LogicException(e);
        }
        return successMarker;
    }


    @Override

    public void deleteNews(Long newsId) throws LogicException {
        if(newsId==null) {
            throw new LogicException(MESSAGE_NO_NEWS);
        }

        try {
            newsDAO.delete(newsId);
        } catch (DAOException e) {
            throw new LogicException(e);
        }
    }

    @Override

    public Long updateNews(NewsDTO newsDTO) throws LogicException {
        Long successMarker = 0L;
        if (NewsCheckUtil.checkNewsDto(newsDTO)) {
            try {
                tagDao.unwireTagsNewsByNews(newsDTO.getNews().getNewsId());
                authorDAO.unwireNewsAuthors(newsDTO.getNews().getNewsId());
                newsDTO.getNews().setNewsModificationDate(Calendar.getInstance().getTime());
                newsDAO.update(newsDTO.getNews());
                newsDAO.addTagsNews(newsDTO.getNews().getNewsId(), newsDTO.getTags());
                newsDAO.addNewsAuthor(newsDTO.getNews().getNewsId(), newsDTO.getAuthor().getAuthorId());
                successMarker = 1L;
            } catch (DAOException e) {
                throw new LogicException(e);
            }
        }
        return successMarker;
    }


    @Override

    public void unwireNewsTags(Long newsId) throws LogicException {
        if(newsId==null) {
            throw new LogicException(MESSAGE_NO_NEWS);
        }
        try {
            tagDao.unwireTagsNewsByNews(newsId);
        } catch (DAOException e) {
            throw new LogicException(e);
        }
    }

    @Override

    public int getSearchNewsQuantity(SearchDTO searchDTO) throws LogicException {
        int quantity;
        try {
            quantity = newsDAO.getTotalNewsQuantity(searchDTO);

        } catch (DAOException e) {
            throw new LogicException(e);
        }
        return quantity;
    }

    @Override

    public NewsDTO getNewsById(Long newsId) throws LogicException {
        NewsDTO newsDTO = new NewsDTO();
        try {
            newsDTO.setNews(newsDAO.findDomainById(newsId));
            newsDTO.setAuthor(authorDAO.findAuthorByNewsId(newsId));
            newsDTO.setTags(tagDao.getTagsByNewsId(newsId));
            newsDTO.setComments(commentDAO.findCommentsByNewsId(newsId));
        } catch (DAOException e) {
            throw new LogicException(e);
        }
        return newsDTO;
    }

    @Override
    public List<NewsDTO> getNewsBySearchCriteria(SearchDTO searchDTO, int page) throws LogicException {
        int startIndex = Integer.valueOf(ConfigurationManager.getProperty(CFG_START));
        int endIndex = Integer.valueOf(ConfigurationManager.getProperty(CFG_END));
        int news = Integer.valueOf(ConfigurationManager.getProperty(CFG));
        int total = getSearchNewsQuantity(searchDTO);
        List<NewsDTO> dtos;
        if (page != 1) {
            startIndex = news * page - news + 1;
            endIndex = news * page;
        }
        if (endIndex > total) {
            endIndex = total;
        }
        try {
            dtos = makeDto(newsDAO.getPaginatedListBySearchCriteria(searchDTO, startIndex, endIndex));

        } catch (DAOException e) {
            throw new LogicException(e);
        }
        return dtos;
    }

    @Override
    public void getNextPrevIDs(SearchDTO searchDTO, Long newsId) throws LogicException {
        if(newsId==null) {
            throw new LogicException(MESSAGE_NO_NEWS);
        }
        try {
            newsDAO.getPrevNextIds(searchDTO, newsId);
        } catch (DAOException e) {
            throw new LogicException(e);
        }
    }

    @Override
    public NewsDTO getNewsByTitle(News news) throws LogicException {
        NewsDTO newsDTO = new NewsDTO();
        if(news.getTitle()==null){
            throw new LogicException(MESSAGE_NO_NEWS);
        }
        try {
            newsDTO.setNews(newsDAO.getNewsByNewsTitle(news));
            newsDTO.setAuthor(authorDAO.findAuthorByNewsId(newsDTO.getNews().getNewsId()));
            newsDTO.setTags(tagDao.getTagsByNewsId(newsDTO.getNews().getNewsId()));
            newsDTO.setComments(commentDAO.findCommentsByNewsId(newsDTO.getNews().getNewsId()));
        } catch (DAOException e) {
            throw new LogicException(e);
        }
        return newsDTO;
    }

    private List<NewsDTO> makeDto(List<News> newses) throws LogicException {
        List<NewsDTO> newsDTOs = new LinkedList<>();
        try {
            for (News news : newses) {
                NewsDTO newsDTO = new NewsDTO();
                newsDTO.setNews(news);
                newsDTO.setAuthor(authorDAO.findAuthorByNewsId(news.getNewsId()));
                newsDTO.setTags(tagDao.getTagsByNewsId(news.getNewsId()));
                newsDTO.setComments(commentDAO.findCommentsByNewsId(news.getNewsId()));
                newsDTOs.add(newsDTO);
            }
        } catch (DAOException e) {
            throw new LogicException(e);
        }
        return newsDTOs;
    }


}
