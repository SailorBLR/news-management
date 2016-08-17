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
    @Autowired
    NewsService newsService;
    @Autowired
    TagService tagService;
    @Autowired
    AuthorService authorService;

    @RequestMapping(value = "/search**", method = RequestMethod.GET)
    public ModelAndView getListOfNews (@RequestParam(value = "nextPage", defaultValue = "1") String nextPage,
                                       @RequestParam(value = "authorsname",defaultValue = "0") String authorsname,
                                       @RequestParam(value = "tagses", defaultValue = "0") String tagses,
                                       @ModelAttribute("searchCriteria") SearchDTO searchDto) throws InternalServerException {
        int page = 1;
        if (!nextPage.equals("1")) {
            page = Integer.valueOf(nextPage);
        }

        if (authorsname!=null&&!authorsname.equals("0")) {
            Author author = new Author();
            author.setAuthorId(Long.valueOf(authorsname));
            searchDto.setAuthor(author);
        }
        if (tagses != null&&!tagses.equals("0")) {
            String str[] = tagses.split(",");
            List<Tag> tagList = new LinkedList<>();
            for (String st : str) {
                Tag tag = new Tag();
                tag.setTagId(Long.valueOf(st));
                tagList.add(tag);
            }
            searchDto.setTags(tagList);
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
}
