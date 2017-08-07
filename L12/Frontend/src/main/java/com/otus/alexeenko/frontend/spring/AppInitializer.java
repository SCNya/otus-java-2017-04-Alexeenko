package com.otus.alexeenko.frontend.spring;

import com.otus.alexeenko.frontend.db.DB;
import com.otus.alexeenko.frontend.templater.PageGenerator;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import javax.servlet.ServletContext;

/**
 * Created by Vsevolod on 14/07/2017.
 */
public class AppInitializer implements WebApplicationInitializer {
    @Override
    public void onStartup(ServletContext container) {
        AnnotationConfigWebApplicationContext context
                = new AnnotationConfigWebApplicationContext();
        context.setConfigLocation("com.otus.alexeenko.l10.spring.WebConfig");
        container.addListener(new ContextLoaderListener(context));
        PageGenerator.setContext(container);
        DB db = DB.getInstance();
        db.run();
    }
}