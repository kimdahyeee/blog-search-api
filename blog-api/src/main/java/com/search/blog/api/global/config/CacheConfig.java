package com.search.blog.api.global.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;
import java.util.HashSet;

@Configuration
@EnableCaching
public class CacheConfig {

    /**
     * redisTemplate 과 다른 점
     * - redis 에 데이터를 직접 저장하고 읽을 수 있고, 다양한 자료구조 사용 가능 - 변경이 많은 경우 적합
     * - cacheManager 는 캐싱된 데이터 를 처리하기 위해 특화됨 - 빈번하게 조회되지만, 변경이 적은 경우 적합
     * -> 결국 cacheManager 도 내부적으로 redisTemplate 을 사용한다.
     *
     * *** 처음에는 간단한 http response 에 캐싱 처리 하기 위해 사용했는데,
     * top 10 에 대해 조금 더 디테일 하게 핸들링하기 위해 redisTemplate 사용하도록 변경
     */
    @Deprecated
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        RedisCacheConfiguration cacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()))
                .entryTtl(Duration.ofMinutes(5));
//                .entryTtl(Duration.ofSeconds(30)); // for test

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(cacheConfiguration)
                .build();
    }

}
