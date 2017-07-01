package com.otus.alexeenko.l9.servlets;

import net.sf.ehcache.management.CacheConfigurationMBean;

import javax.json.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.StringReader;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Vsevolod on 29/06/2017.
 */
public class Management extends HttpServlet implements MyServlet {
    private final Set<String> sessions;
    private final CacheConfigurationMBean configurationMBean;

    public Management(Set<String> sessions, CacheConfigurationMBean configurationMBean) {
        this.sessions = sessions;
        this.configurationMBean = configurationMBean;
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

    @Override
    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {

        if (sessions.contains(request.getSession().getId())) {
            StringReader requestData = new StringReader(request.getReader().lines().collect(Collectors.joining()));

            try (JsonReader jsonReader = Json.createReader(requestData)) {
                JsonObject jData = jsonReader.readObject();
                setValues(jData);
            }
        }

        setOK(response);
    }

    private void setValues(JsonObject jData) {
        configurationMBean.setMemoryStoreEvictionPolicy(jData.getString("policy"));
        configurationMBean.setTimeToLiveSeconds(jData.getInt("time"));
    }

    private String getStatisticJson() {
        JsonObjectBuilder jObjectBuilder = Json.createObjectBuilder();

        jObjectBuilder.add("heapName", "MaxBytesLocalHeap")
                .add("heapSize", configurationMBean.getMaxBytesLocalHeap())
                .add("memoryName", "MemoryStoreEvictionPolicy")
                .add("policy", configurationMBean.getMemoryStoreEvictionPolicy())
                .add("timeName", "TimeToLiveSeconds")
                .add("time", configurationMBean.getTimeToLiveSeconds());

        return jObjectBuilder.build().toString();
    }
}
