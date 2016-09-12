package com.epam.hubarevichadmin.controller;

import com.epam.hubarevich.domain.Author;
import com.epam.hubarevich.domain.Tag;
import com.epam.hubarevich.service.TagService;
import com.epam.hubarevich.service.exception.LogicException;
import com.epam.hubarevichadmin.exception.InternalServerException;
import com.epam.hubarevichadmin.validation.TagValidatorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Controller class for Tags operations
 */
@Controller
@Component
public class TagController {
    private final Integer FIRST_PAGE = 1;
    private final String URL_ALL_TAGS = "/allTags";
    private final String URL_ADD_TAG = "/addTag";
    private final String URL_DELETE_TAG = "/deleteTag";
    private final String URL_UPDATE_TAG = "/updateTag";
    private final String TAG = "tag";
    private final String TAG_ID = "tagId";
    private final String ALL_TAGS = "allTags";
    private final String TAGS = "tags";
    private final String BIND_CLASS = "org.springframework.validation.BindingResult.tag";



    @Autowired
    TagService tagService;
    @Autowired
    TagValidatorUtil tagValidator;

    @InitBinder(TAG)
    private void initCmtBinder(WebDataBinder binder) {
        binder.setValidator(tagValidator);
    }


    /**
     * Gets the view with all of tags to update/delete/add
     *
     * @return ModelAndView object
     */
    @RequestMapping(value = URL_ALL_TAGS, method = RequestMethod.GET)
    public ModelAndView showAll(@ModelAttribute(TAG) Tag tag) throws InternalServerException {

        ModelAndView model = new ModelAndView();
        formModel(model);
        return model;
    }

    /**
     * Creates new Tag object and pass it to Service layer
     *
     * @param tag                Tag object
     * @param bindingResult      object to transfer binding results
     * @param redirectAttributes redirect data (flash message)
     * @return String redirect address
     */
    @RequestMapping(value = URL_ADD_TAG, method = RequestMethod.POST)
    public ModelAndView createNewTag(@Validated @ModelAttribute(TAG) Tag tag,
                                     BindingResult bindingResult,
                                     final RedirectAttributes redirectAttributes) throws InternalServerException {
        ModelAndView modelAndView = new ModelAndView();
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute(BIND_CLASS, bindingResult);
            formModel(modelAndView);
            return modelAndView;
        } else {
            try {
                tagService.addNewTag(tag);
                modelAndView.addObject(TAG, new Tag());
                formModel(modelAndView);
            } catch (LogicException e) {
                throw new InternalServerException(e);
            }
        }

        return modelAndView;
    }

    /**
     * Deletes Tag object from DB
     *
     * @param tagId Long tagId
     * @return String redirect address
     */
    @RequestMapping(value = URL_DELETE_TAG, method = RequestMethod.GET)
    public ModelAndView deleteTag(@RequestParam(value = TAG_ID) Long tagId) throws InternalServerException {

        ModelAndView modelAndView = new ModelAndView();
        try {
            tagService.deleteTag(tagId);
        } catch (LogicException e) {
            throw new InternalServerException(e);
        }
        modelAndView.addObject(TAG,new Tag());
        formModel(modelAndView);
        return modelAndView;
    }

    /**
     * Updates Tag object in DB
     *
     * @param tag                formed Tag object
     * @param bindingResult      object to transfer binding result
     * @param redirectAttributes flash messages
     * @return String redirect to address
     */
    @RequestMapping(value = URL_UPDATE_TAG, method = RequestMethod.POST)
    public ModelAndView updateTag(@Validated @ModelAttribute(TAG) Tag tag,
                                  BindingResult bindingResult,
                                  final RedirectAttributes redirectAttributes) throws InternalServerException {

        ModelAndView modelAndView = new ModelAndView();

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute(BIND_CLASS, bindingResult);
            formModel(modelAndView);
            return modelAndView;
        } else {
            try {
                tagService.updateTag(tag);
                modelAndView.addObject(TAG, new Tag());
                formModel(modelAndView);
            } catch (LogicException e) {
                throw new InternalServerException(e);
            }
        }
        return modelAndView;
    }

    private void formModel(ModelAndView model) throws InternalServerException {
        model.setViewName(ALL_TAGS);
        List<Tag> tags;
        try {
            tags = tagService.getListOfTags();
            model.addObject(TAGS, tags);
        } catch (LogicException e) {
            throw new InternalServerException(e);
        }
    }

}
