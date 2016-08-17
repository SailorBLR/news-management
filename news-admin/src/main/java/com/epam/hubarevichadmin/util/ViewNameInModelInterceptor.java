package com.epam.hubarevichadmin.util;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ViewNameInModelInterceptor extends HandlerInterceptorAdapter {

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {

        if (modelAndView != null) {
            if(request.getParameter("id")!=null){
               request.setAttribute("id",request.getParameter("id"));
            }
            modelAndView.addObject("springViewName", modelAndView.getViewName());
        }
        super.postHandle(request, response, handler, modelAndView);
    }

}