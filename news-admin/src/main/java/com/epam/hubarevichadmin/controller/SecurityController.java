package com.epam.hubarevichadmin.controller;

import com.epam.hubarevich.domain.dto.SearchDTO;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Component
@Controller
@SessionAttributes("searchCriteria")
public class SecurityController {


    private final String URL_LOGIN = "/login";
    private final String URL_403 = "/403";
    private final String LABEL403 = "/403";
    private final String ERROR = "error";
    private final String LOGOUT = "logout";
    private final String LOGIN = "login";
    private final String INVALID_MESSAGE = "Invalid username and password!";
    private final String LOGOUT_MESSAGE = "You've been logged out successfully.";
    private final String MESSAGE = "msg";
    private final String SEARCH_CRITERIA = "searchCriteria";
    private final String USERNAME = "username";



    @RequestMapping(value = URL_LOGIN, method = RequestMethod.GET)
    public ModelAndView login(@RequestParam(value = ERROR, required = false) String error,
                              @RequestParam(value = LOGOUT, required = false) String logout,
                              HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        ModelAndView model = new ModelAndView(LOGIN);

        if (error != null) {
            model.addObject(ERROR, INVALID_MESSAGE);
        }

        if (logout != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
            model.addObject(MESSAGE, LOGOUT_MESSAGE);
        }

        SearchDTO searchDTO = new SearchDTO();
        searchDTO.setNextId(1L);
        model.addObject(SEARCH_CRITERIA, searchDTO);

        return model;

    }

    //for 403 access denied page
    @RequestMapping(value = URL_403, method = RequestMethod.GET)
    public ModelAndView accesssDenied() {

        ModelAndView model = new ModelAndView();

        //check if user is login
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            UserDetails userDetail = (UserDetails) auth.getPrincipal();
            model.addObject(USERNAME, userDetail.getUsername());
        }

        model.setViewName(LABEL403);
        return model;

    }


}
