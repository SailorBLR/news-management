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
    @Autowired
    NewsService newsService;
    @Autowired
    CommentService commentService;

    @RequestMapping(value = "/postComment**", method = RequestMethod.POST)
    public String postComment(@RequestParam(value = "newsId") String newsId,
                              @RequestParam(value = "author") String author,
                              @RequestParam(value = "comment") String text) throws InternalServerException {
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

        return ("redirect:/newsMessage?id="+newsId);
    }

    @RequestMapping(value = "/deleteComment", method = RequestMethod.GET)
    public String deleteComment(@RequestParam(value = "commentId") String commentId,
                                @RequestParam(value = "newsId") Long newsId) throws InternalServerException {

        try {
            commentService.deleteComment(Long.valueOf(commentId));
        } catch (LogicException e) {
            throw new InternalServerException(e);
        }

        return ("redirect:/newsMessage?id="+newsId);
    }
}
