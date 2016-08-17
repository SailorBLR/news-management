package com.epam.hubarevichclient.command.impl;

import com.epam.hubarevich.domain.dto.SearchDTO;
import com.epam.hubarevich.service.NewsService;
import com.epam.hubarevich.service.exception.LogicException;
import com.epam.hubarevichclient.command.ActionCommand;
import com.epam.hubarevichclient.command.exception.CommandException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Anton_Hubarevich on 7/18/2016.
 */
@Component
public class NewsMessage implements ActionCommand {
    @Autowired
    NewsService newsService;
    @Override
    public String execute(HttpServletRequest request) throws CommandException {
        Long newsId = Long.valueOf(request.getParameter("id"));
        SearchDTO searchDTO = (SearchDTO) request.getSession().getAttribute("searchCriteria");

        try {
            newsService.getNextPrevIDs(searchDTO,newsId);
            request.setAttribute("message",newsService.getNewsById(newsId));
            request.setAttribute("title", "News Message");
        } catch (LogicException e) {
            throw new CommandException(e);
        }
        return "includes/newsView.jsp";
    }
}
