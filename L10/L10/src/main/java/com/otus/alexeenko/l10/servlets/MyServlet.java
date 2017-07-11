package com.otus.alexeenko.l10.servlets;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Vsevolod on 27/06/2017.
 */
public interface MyServlet {
    String STATIC = "static";
    String DASHBOARD = "dashboard.html";
    String INDEX = "index.html";

    ApplicationContext context = new ClassPathXmlApplicationContext("L10Beans.xml");
    Set<String> sessions = new HashSet<>();

    default void setOK(HttpServletResponse response) {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
    }

    default void setForbidden(HttpServletResponse response) {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
    }

    default void redirect(HttpServletRequest request, HttpServletResponse response, String pageName) throws IOException {
        if (!request.getRequestURI().equals(pageName))
            response.sendRedirect(response.encodeRedirectURL(request.getContextPath() + "/" + pageName));
    }
}
