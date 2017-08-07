package com.otus.alexeenko.frontend.servlets;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by Vsevolod on 01/07/2017.
 */
public interface MyJsonServlet extends MyServlet {
    @Override
    default void setOK(HttpServletResponse response) {
        response.setContentType("application/json;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    default void setForbidden(HttpServletResponse response) {
        response.setContentType("application/json;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
    }
}
