package com.otus.alexeenko.l10.spring;

import com.otus.alexeenko.l10.db.DB;
import net.sf.ehcache.management.CacheConfigurationMBean;
import net.sf.ehcache.management.CacheStatisticsMBean;
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
