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

    default void setServiceUnavailable(HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
    }
}
