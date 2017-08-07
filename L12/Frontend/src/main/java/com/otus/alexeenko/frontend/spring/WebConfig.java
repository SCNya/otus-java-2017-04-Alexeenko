package com.otus.alexeenko.frontend.spring;

import com.otus.alexeenko.frontend.db.DB;
import net.sf.ehcache.management.CacheConfigurationMBean;
import net.sf.ehcache.management.CacheStatisticsMBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Vsevolod on 15/07/2017.
 */

@Configuration
public class WebConfig {

    @Bean
    public Set<String> sessions() {
        return new HashSet<>();
    }

    @Bean
    public DB dataBase() {
        return DB.getInstance();
    }

    @Bean
    CacheStatisticsMBean statisticsMBean(DB db) {
        return db.getCacheCacheStatisticsMBean();
    }

    @Bean
    CacheConfigurationMBean configurationMBean(DB db) {
        return db.getCacheConfigurationMBean();
    }

    @Bean
    public EventListener eventListener() {
        return new EventListener();
    }
}
