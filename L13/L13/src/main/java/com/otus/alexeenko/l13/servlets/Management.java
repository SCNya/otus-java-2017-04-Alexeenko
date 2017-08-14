package com.otus.alexeenko.l13.servlets;

import com.otus.alexeenko.l13.system.frontend.FrontendMsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Vsevolod on 29/06/2017.
 */

@WebServlet(urlPatterns = {"/management.json"})
public class Management extends HttpServlet implements MyJsonServlet {
    @Autowired
    private Set<String> sessions;
    @Autowired
    private
    FrontendMsgService msgService;

    @Override
    public void init() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        boolean isFoundId = findCookie(sessions, request.getCookies());

        if (isFoundId) {
            String managementInfo = msgService.getManagementInfo();

            if (managementInfo != null) {
                response.getWriter().println(managementInfo);
                setOK(response);
            } else
                setServiceUnavailable(response);
        } else
            setForbidden(response);
    }

    @Override
    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        boolean isFoundId = findCookie(sessions, request.getCookies());

        if (isFoundId) {
            String requestData = request.getReader().lines().collect(Collectors.joining());

            msgService.setConfiguration(requestData);
        }

        setOK(response);
    }
}
