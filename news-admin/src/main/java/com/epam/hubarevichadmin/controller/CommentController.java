package com.epam.hubarevichadmin.controller;

import com.epam.hubarevich.domain.Comment;
import com.epam.hubarevich.service.CommentService;
import com.epam.hubarevich.service.NewsService;
import com.epam.hubarevich.service.exception.LogicException;
import com.epam.hubarevichadmin.exception.InternalServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by Anton_Hubarevich on 7/15/2016.
 */
@Controller
@Component
public class CommentController {
    private final String COMMENT = "comment";
    private final String AUTHOR = "author";
    private final String NEWS_ID = "newsId";
    private final String COMMENT_ID = "commentId";
    private final String URL_POST_COMMENT = "/postComment**";
    private final String URL_NEWS_MESSAGE = "redirect:/newsMessage?id=";
    private final String URL_DELETE_COMMENT = "/deleteComment";


    @Autowired
    NewsService newsService;
    @Autowired
    CommentService commentService;

    @RequestMapping(value = URL_POST_COMMENT , method = RequestMethod.POST)
    public String postComment(@RequestParam(value = NEWS_ID) String newsId,
                              @RequestParam(value = AUTHOR) String author,
                              @RequestParam(value = COMMENT) String text) throws InternalServerException {
        Comment comment = new Comment();
        comment.setNewsId(Long.valueOf(newsId));
        comment.setCommentAuthor(author);
        comment.setCommentText(text);
        comment.setCommentCreationDate();

        try {
            commentService.addComment(comment);
        } catch (LogicException e) {
            throw new InternalServerException(e);
        }

        return (URL_NEWS_MESSAGE.concat(newsId));
    }

    @RequestMapping(value = URL_DELETE_COMMENT, method = RequestMethod.GET)
    public String deleteComment(@RequestParam(value = COMMENT_ID) String commentId,
                                @RequestParam(value = NEWS_ID) String newsId) throws InternalServerException {

        try {
            commentService.deleteComment(Long.valueOf(commentId));
        } catch (LogicException e) {
            throw new InternalServerException(e);
        }

        return (URL_NEWS_MESSAGE.concat(newsId));
    }
}
