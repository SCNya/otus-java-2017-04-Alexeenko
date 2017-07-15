package com.otus.alexeenko.l10.spring;

import com.otus.alexeenko.l10.db.DB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

/**
 * Created by Vsevolod on 15/07/2017.
 */
public class EventListener implements ApplicationListener {
    @Autowired
    DB dataBase;

    public EventListener() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ContextClosedEvent) {
            dataBase.dispose();
        }
    }
}
