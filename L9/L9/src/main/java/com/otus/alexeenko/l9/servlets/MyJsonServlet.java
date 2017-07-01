package com.otus.alexeenko.l9.servlets;

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
}
