package com.epam.hubarevich.service;

import com.epam.hubarevich.domain.News;
import com.epam.hubarevich.domain.dto.NewsDTO;
import com.epam.hubarevich.domain.dto.SearchDTO;
import com.epam.hubarevich.service.exception.LogicException;

import java.util.List;

/**
 * Created by Anton_Hubarevich on 6/23/2016.
 */
public interface NewsService {

    Long createNews(NewsDTO newsDTO) throws LogicException;

    void deleteNews(Long newsId) throws LogicException;

    Long updateNews(NewsDTO newsDTO) throws LogicException;

    void unwireNewsTags(long newsId) throws LogicException;

    int getSearchNewsQuantity(SearchDTO searchDTO) throws LogicException;

    NewsDTO getNewsById(Long newsId)throws LogicException;

    List<NewsDTO> getNewsBySearchCriteria(SearchDTO searchDTO,int page) throws LogicException;

    void getNextPrevIDs(SearchDTO searchDTO,Long newsId) throws LogicException;

    NewsDTO getNewsByTitle(News news) throws LogicException;



}
