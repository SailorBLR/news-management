package com.epam.hubarevich.service.impl;

import com.epam.hubarevich.service.AuthorService;
import com.epam.hubarevich.service.CommentService;
import com.epam.hubarevich.service.NewsService;
import com.epam.hubarevich.service.exception.LogicException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * Service class for news message deleting
 */
@Service
public class NewsDeleteService {

    @Autowired
    NewsService newsService;
    @Autowired
    AuthorService authorService;
    @Autowired
    CommentService commentService;

    /**
     * Deletes all dependencies between News and Tags, Comments, Authors
     * @param newsId
     * @throws LogicException
     */

    public void deleteNewsMessage(long newsId) throws LogicException{

            newsService.unwireNewsTags(newsId);
            authorService.unwireNewsAuthors(newsId);
            commentService.deleteAllCommentsFromNews(newsId);
            newsService.deleteNews(newsId);

    }
}
