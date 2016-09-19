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

import java.util.List;

/**
 * Created by Anton_Hubarevich on 7/15/2016.
 */
@Controller
@Component
@SessionAttributes("searchCriteria")
public class NewsController {
    private final String FIRST_PAGE = "1";
    private final String URL_ALL = "/all**";
    private final String URL_NEWS_MESSAGE = "/newsMessage";
    private final String NEXT_PAGE = "nextPage";
    private final String SEARCH_CRITERIA = "searchCriteria";
    private final String ALL = "all";
    private final String ID = "id";
    private final String PAGES = "pages";
    private final String LIST_NEWS = "listNews";
    private final String TAGS = "tags";
    private final String AUTHORS = "authors";
    private final String NEWS_MESSAGE = "newsMessage";
    private final String MESSAGE = "message";

    @Autowired
    NewsService newsService;
    @Autowired
    TagService tagService;
    @Autowired
    AuthorService authorService;


    @RequestMapping(value = URL_ALL, method = RequestMethod.GET)
    public ModelAndView getListOfNews (@RequestParam(value = NEXT_PAGE, defaultValue = FIRST_PAGE) String nextPage,
                                       @ModelAttribute(SEARCH_CRITERIA) SearchDTO searchDto) throws InternalServerException {
        searchDto.setNull();

        int page = 1;
        if (!nextPage.equals(FIRST_PAGE)) {
            page = Integer.valueOf(nextPage);
        }

        ModelAndView model = new ModelAndView(ALL);

        try {
            List<NewsDTO> newsDTOs = newsService.getNewsBySearchCriteria(searchDto,page);
            List<Tag> tags = tagService.getListOfTags();
            List<Author> authors = authorService.getListOfAuthors();
            int [] pages = TotalNewsQuantityResolverUtil
                    .getTotalPagesQuantity(newsService.getSearchNewsQuantity(searchDto));
            model.addObject(PAGES,pages);
            model.addObject(LIST_NEWS,newsDTOs);
            model.addObject(AUTHORS,authors);
            model.addObject(TAGS,tags);

        } catch (LogicException e) {
            throw new InternalServerException(e);
        }
        return model;
    }


    @RequestMapping(value = URL_NEWS_MESSAGE, method = RequestMethod.GET)
    public  ModelAndView getSingleNewsMessage (@RequestParam(value = ID) Long newsId,
                                               @ModelAttribute(SEARCH_CRITERIA) SearchDTO searchDto) throws InternalServerException {
        ModelAndView model = new ModelAndView(NEWS_MESSAGE);

        try {
            NewsDTO newsDTO = newsService.getNewsById(newsId);
            newsService.getNextPrevIDs(searchDto,newsId);
            model.addObject(MESSAGE,newsDTO);

        } catch (LogicException e) {
            throw new InternalServerException(e);
        }
        return  model;
    }
}
