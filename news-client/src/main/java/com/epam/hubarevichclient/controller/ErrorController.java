package com.epam.hubarevichclient.controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


@WebServlet("/error-controller")
public class ErrorController extends HttpServlet {
    private static final String MAIN_PAGE = "/WEB-INF/jsp/main.jsp";
    private static final String ERROR_PAGE = "/WEB-INF/jsp/includes/error.jsp";
    private static final String INCLUDE = "include";
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processError(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       processError(request,response);
    }

    private void processError(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setAttribute(INCLUDE,ERROR_PAGE);

        RequestDispatcher dispatcher = getServletContext()
                .getRequestDispatcher(MAIN_PAGE);
        try {
            dispatcher.forward(request, response);
        } catch (ServletException e) {
            e.printStackTrace();
        }
    }
}
