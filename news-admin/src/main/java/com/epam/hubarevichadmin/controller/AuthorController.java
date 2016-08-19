package com.epam.hubarevichadmin.controller;

import com.epam.hubarevich.domain.Author;
import com.epam.hubarevich.service.AuthorService;
import com.epam.hubarevich.service.NewsService;
import com.epam.hubarevich.service.exception.LogicException;
import com.epam.hubarevichadmin.exception.InternalServerException;
import com.epam.hubarevichadmin.validation.AuthorValidatorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Created by Anton_Hubarevich on 7/28/2016.
 */
@Controller
@Component
public class AuthorController {
    @Autowired
    AuthorService authorService;
    @Autowired
    NewsService newsService;
    @Autowired
    AuthorValidatorUtil authorValidatorUtil;

    @InitBinder("author")
    private void initCmtBinder(WebDataBinder binder) {
        binder.setValidator(authorValidatorUtil);
    }


    @RequestMapping(value = "/allAuthors", method = RequestMethod.GET)
    public ModelAndView showAuthorsPage(@ModelAttribute("author") Author author
    ) throws InternalServerException {

        ModelAndView modelAndView = new ModelAndView();
        formModel(modelAndView);
        return modelAndView;

    }

    @RequestMapping(value = "/addAuthor", method = RequestMethod.POST)
    public ModelAndView createNewAuthor(@Validated@ModelAttribute("author") Author author,
                                        BindingResult bindingResult,
                                        final RedirectAttributes redirectAttributes, ModelAndView model) throws InternalServerException {

        ModelAndView modelAndView = new ModelAndView();

        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.author", bindingResult);
            formModel(modelAndView);
            return modelAndView;
        }

        try {
            authorService.createAuthor(author);
            modelAndView.addObject("author",new Author());
            formModel(modelAndView);
        } catch (LogicException e) {
            throw new InternalServerException(e);
        }
        return modelAndView;
    }

    @RequestMapping(value = "/updateAuthor", method = RequestMethod.POST)
    public ModelAndView updateAuthor(@Validated@ModelAttribute("author")Author author,
                               BindingResult bindingResult,
                               final RedirectAttributes redirectAttributes) throws InternalServerException {

        ModelAndView modelAndView = new ModelAndView();
        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.author", bindingResult);
            author.setExpired(null);
            formModel(modelAndView);
            return modelAndView;
        }
        try {

            authorService.updateAuthor(author);
            redirectAttributes.addFlashAttribute("author",new Author());
            modelAndView.addObject("author",new Author());
            formModel(modelAndView);
        } catch (LogicException e) {
            throw new InternalServerException(e);
        }
        return modelAndView;
    }



    private void formModel(ModelAndView model) throws InternalServerException {
        model.setViewName("allAuthor");

        List<Author> authors;
        try {
            authors = authorService.getListOfAuthors();
            model.addObject("authors", authors);

        } catch (LogicException e) {
            throw new InternalServerException(e);
        }
    }
}
