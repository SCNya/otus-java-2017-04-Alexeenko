package com.otus.alexeenko.frontend.servlets;

import com.otus.alexeenko.frontend.net.FrontendNetService;
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
 * Created by Vsevolod on 29/06/2017.
 */

@WebServlet(urlPatterns = {"/statistics.json"})
public class Statistics extends HttpServlet implements MyJsonServlet {
    @Autowired
    private Set<String> sessions;
    @Autowired
    FrontendNetService netService;

    @Override
    public void init() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        boolean isFoundId = findCookie(sessions, request.getCookies());

        if (isFoundId) {
            response.getWriter().println(netService.getStatistics());
            setOK(response);
        } else
            setForbidden(response);
    }
}