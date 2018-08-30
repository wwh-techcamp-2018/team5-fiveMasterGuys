package com.woowahan.techcamp.recipehub.common.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableCaching
@EnableScheduling
@Slf4j
public class CachingConfig {
    public static final String CATEGORIES = "categories";

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager(CATEGORIES);
    }

    @CacheEvict(allEntries = true, value = {CATEGORIES})
    @Scheduled(fixedDelay = 5 * 1000 * 60, initialDelay = 500)
    public void evictCategoriesCache() {
        log.debug("Flushed categories cache");
    }
}