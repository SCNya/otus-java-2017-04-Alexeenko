package com.otus.alexeenko.l13.spring;

import com.otus.alexeenko.l13.system.EmbeddedMsgSystem;
import com.otus.alexeenko.l13.system.frontend.EmbeddedMsgFrontendService;
import com.otus.alexeenko.l13.system.frontend.FrontendMsgService;
import com.otus.alexeenko.msg.MsgSystem;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Vsevolod on 15/07/2017.
 */

@Configuration
public class WebConfig {
    @Bean
    public Set<String> sessions() {
        return ConcurrentHashMap.newKeySet();
    }

    @Bean
    public MsgSystem msgSystem() {
        return EmbeddedMsgSystem.getInstance();
    }

    @Bean
    public FrontendMsgService msgService() {
        return EmbeddedMsgFrontendService.getInstance();
    }

    @Bean
    public EventListener eventListener() {
        return new EventListener();
    }
}
