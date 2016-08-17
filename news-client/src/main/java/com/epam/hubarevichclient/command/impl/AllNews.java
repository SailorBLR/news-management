package com.epam.hubarevichclient.command.impl;

import com.epam.hubarevich.domain.Author;
import com.epam.hubarevich.domain.Tag;
import com.epam.hubarevich.domain.dto.NewsDTO;
import com.epam.hubarevich.domain.dto.SearchDTO;
import com.epam.hubarevich.service.AuthorService;
import com.epam.hubarevich.service.NewsService;
import com.epam.hubarevich.service.TagService;
import com.epam.hubarevich.service.exception.LogicException;
import com.epam.hubarevich.utils.TotalNewsQuantityResolverUtil;
import com.epam.hubarevichclient.command.ActionCommand;
import com.epam.hubarevichclient.command.exception.CommandException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedList;
import java.util.List;

/**
 * Action command implementation. Command AllNews
 * Shows all news, according to SearchDto
 */

@Component
public class AllNews implements ActionCommand {
    @Autowired
    NewsService newsService;
    @Autowired
    AuthorService authorService;
    @Autowired
    TagService tagService;

    private final String PAGE = "includes/allNews.jsp";
    private final String PAGES = "pages";
    private final String AUTHORSNAME = "authorsname";
    private final String SEARCHDTO = "searchCriteria";
    private final String TAGS_S = "tagses";
    private final String TAGS = "tags";
    private final String NEXT_PAGE = "nextPage";
    private final String AUTHORS = "authors";
    private final String NEWS_LIST = "newsList";
    private final String TITLE = "title";
    private final String ALL_NEWS = "All News";


    @Override
    public String execute(HttpServletRequest request) throws CommandException {
        String nextPage = request.getParameter(NEXT_PAGE);

        int page = 1;
        if (!nextPage.equals("1")) {
            page = Integer.valueOf(nextPage);
        }
        SearchDTO searchDTO = (SearchDTO) request.getSession().getAttribute(SEARCHDTO);
        if (request.getParameter(AUTHORSNAME) != null && !request.getParameter(AUTHORSNAME).equals("0")) {
            Author author = new Author();
            author.setAuthorId(Long.valueOf(request.getParameter(AUTHORSNAME)));
            searchDTO.setAuthor(author);
        }
        if (request.getParameter(TAGS_S) != null) {
            String str[] = request.getParameterValues(TAGS_S);
            List<Tag> tagList = new LinkedList<>();
            for (String st : str) {
                Tag tag = new Tag();
                tag.setTagId(Long.valueOf(st));
                tagList.add(tag);
            }
            searchDTO.setTags(tagList);
        }

        List<Tag> tags;
        List<NewsDTO> newses;
        List<Author> authors;
        request.getSession().setAttribute(SEARCHDTO, searchDTO);

        try {
            newses = newsService.getNewsBySearchCriteria(searchDTO, page);
            authors = authorService.getListOfAuthors();
            tags = tagService.getListOfTags();
            request.setAttribute(NEWS_LIST, newses);
            request.setAttribute(AUTHORS, authors);
            request.setAttribute(TAGS, tags);
            request.setAttribute(TITLE, ALL_NEWS);
            request.setAttribute
                    (PAGES, TotalNewsQuantityResolverUtil.getTotalPagesQuantity
                            (newsService.getSearchNewsQuantity(searchDTO)));

        } catch (LogicException e) {
            throw new CommandException(e);
        }
        return PAGE;
    }


}
