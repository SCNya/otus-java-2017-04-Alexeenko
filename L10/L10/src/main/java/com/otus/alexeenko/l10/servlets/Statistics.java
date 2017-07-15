package com.otus.alexeenko.l10.servlets;

import net.sf.ehcache.management.CacheStatisticsMBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * Created by Vsevolod on 29/06/2017.
 */

@WebServlet(urlPatterns = {"/statistics.json"})
public class Statistics extends HttpServlet implements MyJsonServlet {
    @Autowired
    private Set<String> sessions;
    @Autowired
    private CacheStatisticsMBean statisticsMBean;

    @Override
    public void init() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {

        if (sessions.contains(request.getSession().getId())) {
            response.getWriter().println(getStatisticJson());
            setOK(response);
        } else
            setForbidden(response);
    }

    private String getStatisticJson() {
        JsonArrayBuilder jArrayBuilder = Json.createArrayBuilder();

        jArrayBuilder.add(
                Json.createObjectBuilder().add("name", "AssociatedCacheName")
                        .add("value", statisticsMBean.getAssociatedCacheName())
        );
        jArrayBuilder.add(
                Json.createObjectBuilder().add("name", "CacheHits")
                        .add("value", statisticsMBean.getCacheHits())
        );
        jArrayBuilder.add(
                Json.createObjectBuilder().add("name", "CacheMisses")
                        .add("value", statisticsMBean.getCacheMisses())
        );
        jArrayBuilder.add(
                Json.createObjectBuilder().add("name", "CacheHitPercentage")
                        .add("value", Math.round(statisticsMBean.getCacheHitPercentage() * 100d))
        );
        jArrayBuilder.add(
                Json.createObjectBuilder().add("name", "CacheMissPercentage")
                        .add("value", Math.round(statisticsMBean.getCacheMissPercentage() * 100d))
        );

        return jArrayBuilder.build().toString();
    }
}