package com.otus.alexeenko.l9;

import com.otus.alexeenko.l9.servlets.*;
import net.sf.ehcache.management.CacheConfigurationMBean;
import net.sf.ehcache.management.CacheStatisticsMBean;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;

import javax.management.JMX;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.util.HashSet;
import java.util.Set;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by Vsevolod on 01/07/2017.
 */
public class MyServer {
    private final static String CACHE_STATISTICS_NAME =
            "net.sf.ehcache:CacheManager=__DEFAULT__,name=userCache,type=CacheStatistics";
    private final static String CACHE_CONFIGURATION_NAME =
            "net.sf.ehcache:CacheManager=__DEFAULT__,name=userCache,type=CacheConfiguration";

    private static final int PORT = 8080;
    private static final Logger MAIN_LOGGER = getLogger("Main");

    private static Server server;

    public void start() throws Exception {
        ServletContextHandler context = getServletContextHandlers();

        server.setHandler(context);
        server.start();

        MAIN_LOGGER.info("Server started");
    }

    private ServletContextHandler getServletContextHandlers() {
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        CacheStatisticsMBean cacheStatisticsMBean = getCacheCacheStatisticsMBean(mbs);
        CacheConfigurationMBean cacheConfigurationMBean = getCacheConfigurationMBean(mbs);

        Set<String> sessions = new HashSet<>();
        Grabber grabberServlet = new Grabber(sessions);
        Index indexServlet = new Index(sessions);
        Dashboard dashboardServlet = new Dashboard(sessions);
        Statistics statisticsServlet = new Statistics(sessions, cacheStatisticsMBean);
        Management managementServlet = new Management(sessions, cacheConfigurationMBean);

        ServletHolder staticServletHolder = new ServletHolder("static", DefaultServlet.class);
        staticServletHolder.setInitParameter("resourceBase", "templates/static");

        server = new Server(PORT);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);

        context.addServlet(staticServletHolder, "/dashboard.js");
        context.addServlet(staticServletHolder, "/libs/json2html.js");
        context.addServlet(staticServletHolder, "/libs/jquery.json2html.js");
        context.addServlet(staticServletHolder, "/css/style.css");
        context.addServlet(new ServletHolder(grabberServlet), "/*");
        context.addServlet(new ServletHolder(indexServlet), "/index.html");
        context.addServlet(new ServletHolder(dashboardServlet), "/dashboard.html");
        context.addServlet(new ServletHolder(statisticsServlet), "/statistics.json");
        context.addServlet(new ServletHolder(managementServlet), "/management.json");

        return context;
    }

    private CacheStatisticsMBean getCacheCacheStatisticsMBean(MBeanServer mbs) {
        CacheStatisticsMBean cacheStatisticsMBean = null;
        ObjectName cacheStatisticsObjectName;

        try {
            cacheStatisticsObjectName = new ObjectName(CACHE_STATISTICS_NAME);
            cacheStatisticsMBean = JMX.newMBeanProxy(mbs, cacheStatisticsObjectName,
                    CacheStatisticsMBean.class, true);
        } catch (MalformedObjectNameException e) {
            e.printStackTrace();
        }

        return cacheStatisticsMBean;
    }

    private CacheConfigurationMBean getCacheConfigurationMBean(MBeanServer mbs) {
        CacheConfigurationMBean cacheConfigurationMBean = null;
        ObjectName cacheConfigurationObjectName;

        try {
            cacheConfigurationObjectName = new ObjectName(CACHE_CONFIGURATION_NAME);
            cacheConfigurationMBean = JMX.newMBeanProxy(mbs, cacheConfigurationObjectName,
                    CacheConfigurationMBean.class, true);
        } catch (MalformedObjectNameException e) {
            e.printStackTrace();
        }
        return cacheConfigurationMBean;
    }

    public void stop() throws Exception {
        server.stop();
    }
}