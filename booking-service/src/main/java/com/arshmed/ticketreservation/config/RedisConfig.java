package com.arshmed.ticketreservation.config;

import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;
import java.time.Duration;

@Configuration
@EnableCaching
public class RedisConfig {

    @Bean
    public RedisCacheConfiguration cacheConfiguration() {
        // Bu bean, Spring Boot'un otomatik olarak bulup kullanacağı varsayılan cache yapılandırmasıdır.
        // Tek görevi, Redis'e yazılacak değerlerin JSON formatında olmasını ve null değerlerin cache'lenmemesini sağlamaktır.
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(60)) // Varsayılan cache süresi
                .disableCachingNullValues()
                .serializeValuesWith(SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
    }

    @Bean
    public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
        // Bu bean, isme özel cache'ler için farklı yaşam süreleri (TTL) belirlememizi sağlar.
        // Yapılandırmanın geri kalanını (JSON serializer gibi) yukarıdaki varsayılan bean'den miras alır.
        return (builder) -> builder
                .withCacheConfiguration("flights",
                        RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(10)))
                .withCacheConfiguration("flight_id",
                        RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofHours(2)))
                .withCacheConfiguration("airports",
                        RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofHours(12)))
                .withCacheConfiguration("aircrafts",
                        RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofHours(12)));
    }
}