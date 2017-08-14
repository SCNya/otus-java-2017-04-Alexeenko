package com.otus.alexeenko.l13.spring;

import com.otus.alexeenko.l13.system.EmbeddedMsgSystem;
import com.otus.alexeenko.l13.templater.PageGenerator;
import com.otus.alexeenko.msg.MsgSystem;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import javax.servlet.ServletContext;

/**
 * Created by Vsevolod on 14/07/2017.
 */
public class AppInitializer implements WebApplicationInitializer {
    static {
        org.apache.log4j.BasicConfigurator.configure();
    }

    @Override
    public void onStartup(ServletContext container) {
        AnnotationConfigWebApplicationContext context
                = new AnnotationConfigWebApplicationContext();
        context.setConfigLocation(WebConfig.class.getCanonicalName());
        container.addListener(new ContextLoaderListener(context));
        PageGenerator.setContext(container);
        MsgSystem msgSystem = EmbeddedMsgSystem.getInstance();
        msgSystem.start();
    }
}