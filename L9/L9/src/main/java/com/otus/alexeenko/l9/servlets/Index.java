package com.otus.alexeenko.l9.servlets;

import com.otus.alexeenko.l9.templater.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * Created by Vsevolod on 26/06/2017.
 */
public class Index extends HttpServlet implements MyServlet {
    private final Set<String> sessions;

    public Index(Set<String> sessions) {
        this.sessions = sessions;
    }

    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {

        Map<String, Object> pageVariables = createPageVariablesMap(request);

        if (sessions.contains(request.getSession().getId())) {
            redirect(request, response, DASHBOARD);
        } else {
            response.getWriter().println(PageGenerator.instance().getPage(INDEX, pageVariables));
        }

        setOK(response);
    }

    @Override
    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> pageVariables = createPageVariablesMap(request);

        String login = request.getParameter("login");
        String password = request.getParameter("password");

        if (login.toUpperCase().equals("ADMIN") &&
                password.toUpperCase().equals("ADMIN")) {
            sessions.add(request.getSession().getId());
            redirect(request, response, DASHBOARD);
        } else {
            pageVariables.put("login", request.getParameter("login"));
            pageVariables.put("message", "The username or password is incorrect");
            response.getWriter().println(PageGenerator.instance().getPage(INDEX, pageVariables));
        }

        setOK(response);
    }

    private static Map<String, Object> createPageVariablesMap(HttpServletRequest request) {
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("login", "");
        pageVariables.put("message", "");
        pageVariables.put("parameters", request.getParameterMap().toString());
        return pageVariables;
    }
}
