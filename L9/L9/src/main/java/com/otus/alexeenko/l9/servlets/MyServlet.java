package com.otus.alexeenko.l9.servlets;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Vsevolod on 27/06/2017.
 */
public interface MyServlet {
    String DASHBOARD = "dashboard.html";
    String INDEX = "index.html";

    default void setOK(HttpServletResponse response) {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
    }

    default void redirect(HttpServletRequest request, HttpServletResponse response, String pageName) throws IOException {
        if (!request.getRequestURI().equals(pageName))
            response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/" + pageName));
    }
}
