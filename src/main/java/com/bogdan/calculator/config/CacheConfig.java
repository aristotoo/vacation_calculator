package com.bogdan.calculator.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * Configuration class for caching in the application.
 * This class configures the Caffeine cache manager with specific settings
 * for cache expiration and size limits.
 */
@Configuration
@EnableCaching
public class CacheConfig {

    /**
     * Creates and configures the cache manager bean.
     * The cache is configured with the following settings:
     * - Cache entries expire after 24 hours
     * - Maximum cache size is 1000 entries
     * - Cache statistics are recorded
     *
     * @return The configured cache manager
     */
    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .expireAfterWrite(24, TimeUnit.HOURS)
                .maximumSize(1000)
                .recordStats());
        return cacheManager;
    }
} 