package com.otus.alexeenko.frontend.servlets;

import com.otus.alexeenko.frontend.templater.PageGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * Created by Vsevolod on 11/07/2017.
 */

@WebServlet(urlPatterns = {"/css/style.css"})
public class StaticCSS extends HttpServlet implements MyServlet {
    @Autowired
    private PageGenerator pageGenerator;

    @Autowired
    private Set<String> sessions;

    @Override
    public void init() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        boolean isFoundId = findCookie(sessions, request.getCookies());

        if (isFoundId) {
            response.getWriter().println(pageGenerator.getPage(STATIC + request.getRequestURI()));
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
