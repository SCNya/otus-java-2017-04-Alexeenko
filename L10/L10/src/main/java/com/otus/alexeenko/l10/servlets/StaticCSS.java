package com.otus.alexeenko.l10.servlets;

import com.otus.alexeenko.l10.templater.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Vsevolod on 11/07/2017.
 */

@WebServlet(urlPatterns = {"/css/style.css"})
public class StaticCSS extends HttpServlet implements MyServlet {

    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {

        if (sessions.contains(request.getSession().getId())) {
            response.getWriter().println(PageGenerator.instance().getPage(STATIC + request.getRequestURI()));
            setOK(response);
        } else
            setForbidden(response);
    }

    @Override
    public void setOK(HttpServletResponse response) {
        response.setContentType("text/css;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    public void setForbidden(HttpServletResponse response) {
        response.setContentType("text/css;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
    }
}
