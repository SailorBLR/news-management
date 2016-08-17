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

    @InitBinder("newsDto")
    private void initCmtBinder(WebDataBinder binder) {
        binder.setValidator(newsValidatorUtil);
    }


    @RequestMapping(value = "/addNews", method = RequestMethod.GET)
    public ModelAndView addSingleNewsMessage(@RequestParam(value = "id", defaultValue = "0") Long newsId) throws InternalServerException {
        ModelAndView model = new ModelAndView("addNews");
        NewsDTO newsDTO=null;

        try {
            if (newsId != 0L) {
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

    @RequestMapping(value = "/addNewMessage", method = RequestMethod.POST)
    public ModelAndView addNewMessage(@Validated@ModelAttribute("newsDto") NewsDTO newsDTO,
                                BindingResult result, RedirectAttributes redirectAttributes) throws InternalServerException {

        ModelAndView model = new ModelAndView();


        if (result.hasErrors()) {
            model.setViewName("addNews");
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.newsDto", result);
            formNewsModel(model,newsDTO);
            return model;
        }

        model.setViewName("newsMessage");
        try {
            if(newsDTO.getNews().getNewsId()!=null){
               newsService.updateNews(newsDTO);
                model.addObject("message",newsService.getNewsById(newsDTO.getNews().getNewsId()));
            } else {
                newsService.createNews(newsDTO);

                model.addObject("message",newsService.getNewsByTitle(newsDTO.getNews()));
            }

        } catch (LogicException e) {
            throw new InternalServerException(e);
        }
        return model;
    }

    @RequestMapping(value = "/deleteMessage", method = RequestMethod.GET)
    public String deleteMessage(@RequestParam(value = "id", defaultValue = "0") Long newsId) throws InternalServerException {
        try {
            if (newsId != null) {
                newsDeleteService.deleteNewsMessage(newsId);
            }
        } catch (LogicException e) {
            throw new InternalServerException(e);
        }
        return "redirect: /allNews";
    }

    private void formNewsModel(ModelAndView model,NewsDTO newsDTO) throws InternalServerException {

        List<Author> authors;
        List<Tag> tags;
        try {
            model.addObject("newsDto",newsDTO);
            authors = authorService.getListAvailableAuthors();
            tags = tagService.getListOfTags();
            model.addObject("authorList", authors);
            model.addObject("tagList", tags);
        }catch (LogicException e){
            throw new InternalServerException(e);
        }


    }

}
