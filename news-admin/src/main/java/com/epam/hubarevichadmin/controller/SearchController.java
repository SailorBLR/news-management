package com.epam.hubarevichadmin.controller;

import com.epam.hubarevich.domain.Author;
import com.epam.hubarevich.domain.Tag;
import com.epam.hubarevich.domain.dto.NewsDTO;
import com.epam.hubarevich.domain.dto.SearchDTO;
import com.epam.hubarevich.service.AuthorService;
import com.epam.hubarevich.service.NewsService;
import com.epam.hubarevich.service.TagService;
import com.epam.hubarevich.service.exception.LogicException;
import com.epam.hubarevich.utils.TotalNewsQuantityResolverUtil;
import com.epam.hubarevichadmin.exception.InternalServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Anton_Hubarevich on 7/28/2016.
 */
@Component
@Controller
@SessionAttributes("searchCriteria")
public class SearchController {

    private final String DEFAULT_VALUE_0 = "0";
    private final String DEFAULT_VALUE_1 = "1";
    private final String COMMA = ",";
    private final String URL_SEARCH = "/search**";
    private final String ALL = "all";
    private final String AUTHORSNAME = "authorsname";
    private final String SEARCH_CRITERIA = "searchCriteria";
    private final String NEXT_PAGE = "nextPage";
    private final String TAGSES = "tagses";
    private final String PAGES = "pagesQuantity";
    private final String AUTHORS = "authors";
    private final String TAGS = "tags";
    private final String LIST_NEWS = "listNews";

    @Autowired
    NewsService newsService;
    @Autowired
    TagService tagService;
    @Autowired
    AuthorService authorService;

    @RequestMapping(value = URL_SEARCH, method = RequestMethod.GET)
    public ModelAndView getListOfNews (@RequestParam(value = NEXT_PAGE, defaultValue = DEFAULT_VALUE_1) String nextPage,
                                       @RequestParam(value = AUTHORSNAME,defaultValue = DEFAULT_VALUE_0) String authorsname,
                                       @RequestParam(value = TAGSES, defaultValue = DEFAULT_VALUE_0) String tagses,
                                       @ModelAttribute(SEARCH_CRITERIA) SearchDTO searchDto) throws InternalServerException {
        int page = 1;
        if (!nextPage.equals(DEFAULT_VALUE_1)) {
            page = Integer.valueOf(nextPage);
        }

        if (authorsname!=null&&!authorsname.equals(DEFAULT_VALUE_0)) {
            Author author = new Author();
            author.setAuthorId(Long.valueOf(authorsname));
            searchDto.setAuthor(author);
        }
        if (tagses != null&&!tagses.equals(DEFAULT_VALUE_0)) {
            String str[] = tagses.split(COMMA);
            List<Tag> tagList = new LinkedList<>();
            for (String st : str) {
                Tag tag = new Tag();
                tag.setTagId(Long.valueOf(st));
                tagList.add(tag);
            }
            searchDto.setTags(tagList);
        }

        ModelAndView model = new ModelAndView(ALL);

        try {
            List<NewsDTO> newsDTOs = newsService.getNewsBySearchCriteria(searchDto,page);
            List<Tag> tags = tagService.getListOfTags();
            List<Author> authors = authorService.getListOfAuthors();

            int pages = TotalNewsQuantityResolverUtil
                    .getTotalPagesQuantity(newsService.getSearchNewsQuantity(searchDto));
            model.addObject(NEXT_PAGE,page);
            model.addObject(PAGES,pages);
            model.addObject(LIST_NEWS,newsDTOs);
            model.addObject(AUTHORS,authors);
            model.addObject(TAGS,tags);

        } catch (LogicException e) {
            throw new InternalServerException(e);
        }
        return model;

    }
}
