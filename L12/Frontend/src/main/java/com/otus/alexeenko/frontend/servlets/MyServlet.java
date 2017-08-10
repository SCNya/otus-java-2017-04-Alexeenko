package com.otus.alexeenko.frontend.servlets;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * Created by Vsevolod on 27/06/2017.
 */
public interface MyServlet {
    String STATIC = "static";
    String DASHBOARD = "dashboard.html";
    String INDEX = "index.html";

    default boolean findCookie(Set<String> sessions, Cookie[] cookies) {
        if (cookies != null) {
            for (Cookie cookie : cookies)
                if (sessions.contains(cookie.getName()))
                    return true;
        }

        return false;
    }

    default void setOK(HttpServletResponse response) {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
    }

    default void setForbidden(HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
    }

    default void redirect(HttpServletRequest request, HttpServletResponse response, String pageName) throws IOException {
        if (!request.getRequestURI().equals(pageName))
            response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/" + pageName));
    }
}
