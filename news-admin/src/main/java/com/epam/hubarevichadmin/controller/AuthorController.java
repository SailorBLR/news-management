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
    public ModelAndView showAuthorsPage(@ModelAttribute("author") Author author) throws InternalServerException {
        return formModel();
    }


    @RequestMapping(value = "/addAuthor", method = RequestMethod.GET)
    public ModelAndView createNewAuthor(@Validated@ModelAttribute("author") Author author,
                                  BindingResult bindingResult,
                                  final RedirectAttributes redirectAttributes) throws InternalServerException {


        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.author", bindingResult);
            return formModel();
        }
        try {
            authorService.createAuthor(author);

        } catch (LogicException e) {
            throw new InternalServerException(e);
        }
        return formModel();
    }

    @RequestMapping(value = "/updateAuthor", method = RequestMethod.GET)
    public ModelAndView updateAuthor(@Validated@ModelAttribute("author")Author author,
                               BindingResult bindingResult,
                               final RedirectAttributes redirectAttributes) throws InternalServerException {

        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.author", bindingResult);
            return formModel();
        }
        try {
            authorService.updateAuthor(author);
        } catch (LogicException e) {
            e.printStackTrace();
        }
        return formModel();
    }

    private ModelAndView formModel() throws InternalServerException {
        ModelAndView model = new ModelAndView("allAuthors");
        List<Author> authors;
        try {
            authors = authorService.getListOfAuthors();
            model.addObject("authors", authors);
        } catch (LogicException e) {
            throw new InternalServerException(e);
        }
        return model;
    }
}
