package com.epam.hubarevichadmin.controller;

import com.epam.hubarevichadmin.exception.InternalServerException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Anton_Hubarevich on 8/10/2016.
 */
@ControllerAdvice
public class ExceptionController {



    @ExceptionHandler(InternalServerException.class)
    public ModelAndView handleServiceException(InternalServerException exc){

        ModelAndView model = new ModelAndView("500");
        model.addObject("message", exc);
        return model;
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ModelAndView handlePermissionException(AccessDeniedException exc){

        ModelAndView model = new ModelAndView("403");
        model.addObject("message", exc);
        return model;
    }


}
