package com.epam.hubarevichadmin.controller;

import com.epam.hubarevich.domain.Tag;
import com.epam.hubarevich.service.TagService;
import com.epam.hubarevich.service.exception.LogicException;
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
    @Autowired
    TagService tagService;
    @Autowired
    TagValidatorUtil tagValidator;

    @InitBinder("tag")
    private void initCmtBinder(WebDataBinder binder) {
        binder.setValidator(tagValidator);
    }


    /**
     * Gets the view with all of tags to update/delete/add
     *
     * @return ModelAndView object
     */
    @RequestMapping(value = "/allTags", method = RequestMethod.GET)
    public ModelAndView showAll() {
        List<Tag> tags;
        ModelAndView model = new ModelAndView("allTags");

        try {
            tags = tagService.getListOfTags();
            model.addObject("tags", tags);
            model.addObject("tag", new Tag());
        } catch (LogicException e) {
            e.printStackTrace();
        }
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
    @RequestMapping(value = "/addTag", method = RequestMethod.GET)
    public String createNewTag(@Validated @ModelAttribute("tag") Tag tag,
                               BindingResult bindingResult,
                               final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.tag", bindingResult);
            return ("redirect:/allTags");
        } else {
            try {
                tagService.addNewTag(tag);
            } catch (LogicException e) {
                e.printStackTrace();
            }
        }
        return ("redirect:/allTags");
    }

    /**
     * Deletes Tag object from DB
     *
     * @param tagId Long tagId
     * @return String redirect address
     */
    @RequestMapping(value = "/deleteTag", method = RequestMethod.GET)
    public String deleteTag(@RequestParam(value = "tagId") Long tagId) {

        try {
            tagService.deleteTag(tagId);
        } catch (LogicException e) {
            e.printStackTrace();
        }


        return ("redirect:/allTags");
    }

    /**
     * Updates Tag object in DB
     * @param tag formed Tag object
     * @param bindingResult object to transfer binding result
     * @param redirectAttributes flash messages
     * @return String redirect to address
     */
    @RequestMapping(value = "/updateTag", method = RequestMethod.GET)
    public String updateTag(@Validated @ModelAttribute("tag") Tag tag,
                            BindingResult bindingResult,
                            final RedirectAttributes redirectAttributes) {


        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.tag", bindingResult);
            return ("redirect:/allTags");
        } else {
            try {
                tagService.updateTag(tag);
            } catch (LogicException e) {
                e.printStackTrace();
            }
        }
        return ("redirect:/allTags");
    }

}
