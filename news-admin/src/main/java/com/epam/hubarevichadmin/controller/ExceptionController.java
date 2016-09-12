package com.epam.hubarevichadmin.controller;

import com.epam.hubarevichadmin.exception.InternalServerException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;


@ControllerAdvice
public class ExceptionController {
    private final String MESSAGE = "message";
    private final String CODE_500 = "500";
    private final String CODE_403 = "403";


    @ExceptionHandler(InternalServerException.class)
    public ModelAndView handleServiceException(InternalServerException exc){

        ModelAndView model = new ModelAndView(CODE_500);
        model.addObject(MESSAGE, exc);
        return model;
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ModelAndView handlePermissionException(AccessDeniedException exc){

        ModelAndView model = new ModelAndView(CODE_403);
        model.addObject(MESSAGE, exc);
        return model;
    }


}
