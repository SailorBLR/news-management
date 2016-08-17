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
    @Autowired
    NewsService newsService;
    @Autowired
    TagService tagService;
    @Autowired
    AuthorService authorService;


    @RequestMapping(value = "/all**", method = RequestMethod.GET)
    public ModelAndView getListOfNews (@RequestParam(value = "nextPage", defaultValue = "1") String nextPage,
                                       @ModelAttribute("searchCriteria") SearchDTO searchDto) throws InternalServerException {
        searchDto.setNull();

        int page = 1;
        if (!nextPage.equals("1")) {
            page = Integer.valueOf(nextPage);
        }

        ModelAndView model = new ModelAndView("all");

        try {
            List<NewsDTO> newsDTOs = newsService.getNewsBySearchCriteria(searchDto,page);
            List<Tag> tags = tagService.getListOfTags();
            List<Author> authors = authorService.getListOfAuthors();
            int [] pages = TotalNewsQuantityResolverUtil
                    .getTotalPagesQuantity(newsService.getSearchNewsQuantity(searchDto));
            model.addObject("pages",pages);
            model.addObject("listNews",newsDTOs);
            model.addObject("authors",authors);
            model.addObject("tags",tags);

        } catch (LogicException e) {
            throw new InternalServerException(e);
        }
        return model;
    }


    @RequestMapping(value = "/newsMessage", method = RequestMethod.GET)
    public  ModelAndView getSingleNewsMessage (@RequestParam(value = "id") Long newsId,
                                               @ModelAttribute("searchCriteria") SearchDTO searchDto) throws InternalServerException {
        ModelAndView model = new ModelAndView("newsMessage");

        try {
            NewsDTO newsDTO = newsService.getNewsById(newsId);
            newsService.getNextPrevIDs(searchDto,newsId);
            model.addObject("message",newsDTO);

        } catch (LogicException e) {
            throw new InternalServerException(e);
        }
        return  model;
    }
}
