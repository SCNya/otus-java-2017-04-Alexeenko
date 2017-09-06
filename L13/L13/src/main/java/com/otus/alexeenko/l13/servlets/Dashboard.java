package com.otus.alexeenko.l13.servlets;

import com.otus.alexeenko.l13.templater.PageGenerator;
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
 * Created by Vsevolod on 27/06/2017.
 */

@WebServlet(urlPatterns = {"/dashboard.html"})
public class Dashboard extends HttpServlet implements MyServlet {
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
            response.getWriter().println(pageGenerator.getPage(DASHBOARD));
        } else {
            redirect(request, response, INDEX);
        }

        setOK(response);
    }
}