package com.otus.alexeenko.l10.servlets;

import com.otus.alexeenko.l10.templater.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Vsevolod on 27/06/2017.
 */

@WebServlet(urlPatterns = {"/dashboard.html"})
public class Dashboard extends HttpServlet implements MyServlet {

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