package com.microservice.payment.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;

import static com.microservice.payment.utils.Constants.CACHE_PAYMENT;

/**
 * This class resides redis configuration
 * @author Asif Bakht
 * @since 2024
 */
@Configuration
public class RedisConfig {
    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private int redisPort;

    @Value("${cache.payment.time-to-live:10}")
    private int paymentTTL;
    @Value("${cache.default.time-to-live:5}")
    private int defaultTTL;

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        final RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(redisHost, redisPort);
        return new LettuceConnectionFactory(configuration);
    }
    @Bean
    public RedisCacheManager cacheManager() {
        final RedisCacheConfiguration cacheConfig = myDefaultCacheConfig (Duration.ofMinutes(defaultTTL))
                .disableCachingNullValues();
        return RedisCacheManager
                .builder(redisConnectionFactory())
                .cacheDefaults(cacheConfig)
                .withCacheConfiguration(
                        CACHE_PAYMENT,
                        myDefaultCacheConfig(Duration.ofMinutes(paymentTTL))
                )
                .build();
    }

    private RedisCacheConfiguration myDefaultCacheConfig(final Duration duration) {
        return RedisCacheConfiguration
                .defaultCacheConfig()
                .entryTtl(duration)
                .serializeValuesWith(RedisSerializationContext
                        .SerializationPair
                        .fromSerializer(
                                new GenericJackson2JsonRedisSerializer()
                        ));
    }
}