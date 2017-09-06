package com.otus.alexeenko.frontend.spring;

import com.otus.alexeenko.frontend.net.FrontendNetService;
import com.otus.alexeenko.frontend.net.MsgNetFrontendService;
import com.otus.alexeenko.frontend.templater.PageGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.ServletContext;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Vsevolod on 15/07/2017.
 */

@Configuration
public class WebConfig {
    @Bean
    public PageGenerator pageGenerator(ServletContext servletContext) {
        return new PageGenerator(servletContext);
    }

    @Bean
    public Set<String> sessions() {
        return ConcurrentHashMap.newKeySet();
    }

    @Bean(initMethod = "start", destroyMethod = "dispose")
    public FrontendNetService netService() {
        return MsgNetFrontendService.getInstance();
    }
}
