package com.epam.hubarevichclient.command.impl;

import com.epam.hubarevich.domain.Comment;
import com.epam.hubarevich.service.CommentService;
import com.epam.hubarevich.service.NewsService;
import com.epam.hubarevich.service.exception.LogicException;
import com.epam.hubarevich.utils.DateUtil;
import com.epam.hubarevichclient.command.ActionCommand;
import com.epam.hubarevichclient.command.exception.CommandException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Anton_Hubarevich on 7/18/2016.
 */
@Component
public class PostComment implements ActionCommand {
    @Autowired
    NewsService newsService;
    @Autowired
    CommentService commentService;
    @Override
    public String execute(HttpServletRequest request) throws CommandException {
        Long newsId = Long.valueOf(request.getParameter("newsId"));
        Comment comment = new Comment();
        comment.setNewsId(newsId);
        comment.setCommentAuthor(request.getParameter("author"));
        comment.setCommentText(request.getParameter("comment"));
        comment.setCommentCreationDate(DateUtil.makeTimeStampNow());

        try {
            commentService.addComment(comment);
            request.setAttribute("message",newsService.getNewsById(newsId));
        } catch (LogicException e) {
            throw new CommandException(e);
        }
        return "includes/newsView.jsp";
    }
}
