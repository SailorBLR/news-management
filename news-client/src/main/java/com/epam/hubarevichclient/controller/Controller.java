package com.epam.hubarevichclient.controller;


import com.epam.hubarevich.domain.dto.SearchDTO;
import com.epam.hubarevichclient.command.ActionCommand;
import com.epam.hubarevichclient.command.ActionFactory;
import com.epam.hubarevichclient.command.exception.CommandException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
@WebServlet("/controller")
public class Controller extends HttpServlet {
    private final Logger LOG = LogManager.getLogger(Controller.class);
    private final long serialVersionUID = 1L;
    private final String MAIN_PAGE = "/WEB-INF/jsp/main.jsp";
    private final String CLEAR_MARKER = "clearMarker";
    private final String SEARCH_CRITERIA = "searchCriteria";
    private final String TRUE = "true";
    private final String INCLUDE = "include";
    private WebApplicationContext webApplicationContext;

    /**
     * Initializes application servlet and creates Connection Pool
     * @throws ServletException
     */
    @Override
    public void init() throws ServletException {
        super.init();
        webApplicationContext
                = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
    }


    @Override
    public void destroy() {
        super.destroy();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        processRequest(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);

    }

    private void processRequest(HttpServletRequest request,
                                HttpServletResponse response)
            throws ServletException, IOException {

        if(TRUE.equals(request.getParameter(CLEAR_MARKER))
                |request.getSession().getAttribute(SEARCH_CRITERIA)==null){
            SearchDTO searchDTO = new SearchDTO();
            request.getSession().setAttribute(SEARCH_CRITERIA,searchDTO);
        }

        String include=null;
        ActionCommand command = ActionFactory.defineCommand(request,webApplicationContext);

        try {
            include = command.execute(request);

        } catch (CommandException e) {
            throw new ServletException(e);
        }

        request.setAttribute(INCLUDE,include);
        RequestDispatcher dispatcher = getServletContext()
                .getRequestDispatcher(MAIN_PAGE);
        dispatcher.forward(request, response);
    }


}
