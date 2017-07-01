package com.otus.alexeenko.l9.servlets;

import net.sf.ehcache.management.CacheStatisticsMBean;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * Created by Vsevolod on 29/06/2017.
 */
public class Statistics extends HttpServlet implements MyServlet {
    private final Set<String> sessions;
    private final CacheStatisticsMBean statisticsMBean;

    public Statistics(Set<String> sessions, CacheStatisticsMBean statisticsMBean) {
        this.sessions = sessions;
        this.statisticsMBean = statisticsMBean;
    }

    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {

        if (sessions.contains(request.getSession().getId())) {
            response.getWriter().println(getStatisticJson());
        } else {
            redirect(request, response, INDEX);
        }

        setOK(response);
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