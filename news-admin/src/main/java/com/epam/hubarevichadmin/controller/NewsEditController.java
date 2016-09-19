package com.epam.hubarevichadmin.controller;

import com.epam.hubarevich.domain.Author;
import com.epam.hubarevich.domain.Tag;
import com.epam.hubarevich.domain.dto.NewsDTO;
import com.epam.hubarevich.service.AuthorService;
import com.epam.hubarevich.service.NewsService;
import com.epam.hubarevich.service.TagService;
import com.epam.hubarevich.service.exception.LogicException;
import com.epam.hubarevich.service.impl.NewsDeleteService;
import com.epam.hubarevichadmin.exception.InternalServerException;
import com.epam.hubarevichadmin.validation.NewsValidatorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


@EnableWebMvc
@Controller
@Component
@SessionAttributes("searchCriteria")
public class NewsEditController {
    private final String DEFAULT_VALUE = "0";
    private final String URL_ADD_NEWS = "/addNews";
    private final String URL_ADD_NEW_MESSAGE = "/addNewMessage";
    private final String URL_DELETE_MESSAGE = "/deleteMessage";
    private final String URL_ALL_NEWS = "redirect: /allNews";
    private final String AUTHOR_LIST = "authorList";
    private final String TAG_LIST = "tagList";
    private final String ADD_NEWS = "addNews";
    private final String ID = "id";
    private final String BIND_CLASS = "org.springframework.validation.BindingResult.newsDto";
    private final String NEWS_DTO = "newsDto";
    private final String MESSAGE = "message";
    private final String NEWS_MESSAGE = "newsMessage";


    @Autowired
    NewsService newsService;
    @Autowired
    TagService tagService;
    @Autowired
    AuthorService authorService;
    @Autowired
    NewsDeleteService newsDeleteService;
    @Autowired
    NewsValidatorUtil newsValidatorUtil;

    @InitBinder(NEWS_DTO)
    private void initCmtBinder(WebDataBinder binder) {
        binder.setValidator(newsValidatorUtil);
    }


    @RequestMapping(value = URL_ADD_NEWS, method = RequestMethod.GET)
    public ModelAndView addSingleNewsMessage(@RequestParam(value = ID, defaultValue = DEFAULT_VALUE) Long newsId)
            throws InternalServerException {
        ModelAndView model = new ModelAndView(ADD_NEWS);
        NewsDTO newsDTO;

        try {
            if (!Long.valueOf(DEFAULT_VALUE).equals(newsId)) {
                newsDTO = newsService.getNewsById(newsId);
            } else {
                newsDTO = new NewsDTO();
            }
            formNewsModel(model,newsDTO);
        } catch (LogicException e) {
            throw new InternalServerException(e);
        }
        return model;
    }

    @RequestMapping(value = URL_ADD_NEW_MESSAGE, method = RequestMethod.POST)
    public ModelAndView addNewMessage(@Validated@ModelAttribute(NEWS_DTO) NewsDTO newsDTO,
                                BindingResult result, RedirectAttributes redirectAttributes)
            throws InternalServerException {

        ModelAndView model = new ModelAndView();


        if (result.hasErrors()) {
            model.setViewName(ADD_NEWS);
            redirectAttributes.addFlashAttribute(BIND_CLASS, result);
            formNewsModel(model,newsDTO);
            return model;
        }

        model.setViewName(NEWS_MESSAGE);
        try {
            if(newsDTO.getNews().getNewsId()!=null){
               newsService.updateNews(newsDTO);
                model.addObject(MESSAGE,newsService.getNewsById(newsDTO.getNews().getNewsId()));
            } else {
                newsService.createNews(newsDTO);

                model.addObject(MESSAGE,newsService.getNewsByTitle(newsDTO.getNews()));
            }

        } catch (LogicException e) {
            throw new InternalServerException(e);
        }
        return model;
    }

    @RequestMapping(value = URL_DELETE_MESSAGE, method = RequestMethod.GET)
    public String deleteMessage(@RequestParam(value = ID, defaultValue = DEFAULT_VALUE) Long newsId)
            throws InternalServerException {
        try {
            if (newsId != null) {
                newsDeleteService.deleteNewsMessage(newsId);
            }
        } catch (LogicException e) {
            throw new InternalServerException(e);
        }
        return URL_ALL_NEWS;
    }

    private void formNewsModel(ModelAndView model,NewsDTO newsDTO) throws InternalServerException {

        List<Author> authors;
        List<Tag> tags;
        try {
            model.addObject(NEWS_DTO,newsDTO);
            authors = authorService.getListAvailableAuthors();
            tags = tagService.getListOfTags();
            model.addObject(AUTHOR_LIST, authors);
            model.addObject(TAG_LIST, tags);
        }catch (LogicException e){
            throw new InternalServerException(e);
        }


    }

}
