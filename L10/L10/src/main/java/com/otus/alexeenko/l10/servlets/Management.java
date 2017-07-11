package com.otus.alexeenko.l10.servlets;

import com.otus.alexeenko.l10.db.DB;
import net.sf.ehcache.management.CacheConfigurationMBean;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.StringReader;
import java.util.stream.Collectors;

/**
 * Created by Vsevolod on 29/06/2017.
 */

@WebServlet(urlPatterns = {"/management.json"})
public class Management extends HttpServlet implements MyJsonServlet {
    private CacheConfigurationMBean configurationMBean;

    @Override
    public void init() {
        this.configurationMBean = ((DB) context.getBean("dataBase")).getCacheConfigurationMBean();
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
