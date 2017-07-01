package com.otus.alexeenko.l9.servlets;

import com.otus.alexeenko.l9.templater.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * Created by Vsevolod on 27/06/2017.
 */
public class Dashboard extends HttpServlet implements MyServlet {
    private final Set<String> sessions;

    public Dashboard(Set<String> sessions) {
        this.sessions = sessions;
    }

    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {

        if (sessions.contains(request.getSession().getId())) {
            response.getWriter().println(PageGenerator.instance().getPage(DASHBOARD));
        } else {
            redirect(request, response, INDEX);
        }

        setOK(response);
    }
}
