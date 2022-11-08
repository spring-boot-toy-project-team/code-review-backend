package com.codereview.common.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@RequiredArgsConstructor
@Configuration
@EnableRedisRepositories
public class RedisConfig {
  @Value("${spring.redis.host}")
  private String redisHost;

  @Value("${spring.redis.port}")
  private int redisPort;

  @Bean
  public RedisConnectionFactory redisConnectionFactory() {
    return new LettuceConnectionFactory(redisHost, redisPort);
  }

  @Bean
  public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {

    RedisTemplate<String, Object> template = new RedisTemplate<>();
    template.setConnectionFactory(redisConnectionFactory);

    Jackson2JsonRedisSerializer<Object> jacksonSerializer = new Jackson2JsonRedisSerializer<>(Object.class);

    ObjectMapper om = new ObjectMapper();
    om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
    om.registerModule(new Hibernate5Module());
    om.registerModule(new JavaTimeModule());
    jacksonSerializer.setObjectMapper(om);

    template.setValueSerializer(jacksonSerializer);
    template.setKeySerializer(new StringRedisSerializer());

    template.setHashKeySerializer(new StringRedisSerializer());
    template.setHashValueSerializer(jacksonSerializer);
    template.afterPropertiesSet();

    return template;
  }

  @Bean
  public RedisCacheManager cacheManager(RedisConnectionFactory factory) {
    RedisSerializer<String> redisSerializer = new StringRedisSerializer();
    Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);

    ObjectMapper om = new ObjectMapper();
    om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
    om.registerModule(new Hibernate5Module());
    om.registerModule(new JavaTimeModule());
    om.activateDefaultTyping(LaissezFaireSubTypeValidator.instance,ObjectMapper.DefaultTyping.NON_FINAL,
      JsonTypeInfo.As.WRAPPER_ARRAY);
    jackson2JsonRedisSerializer.setObjectMapper(om);

    RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
      .entryTtl(Duration.ofMinutes(30L))
      .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer))
      .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer))
      .disableCachingNullValues();

    return RedisCacheManager.builder(factory)
      .cacheDefaults(config)
      .build();
  }

  @Bean
  public HashOperations<String, String, Object> hashOperations(RedisTemplate<String, Object> redisTemplate) {
    return redisTemplate.opsForHash();
  }

  @Bean
  public ValueOperations<String, Object> valueOperations(RedisTemplate<String, Object> redisTemplate) {
    return redisTemplate.opsForValue();
  }

  @Bean
  public ListOperations<String, Object> listOperations(RedisTemplate<String, Object> redisTemplate) {
    return redisTemplate.opsForList();
  }

  @Bean
  public SetOperations<String, Object> setOperations(RedisTemplate<String, Object> redisTemplate) {
    return redisTemplate.opsForSet();
  }

  @Bean
  public ZSetOperations<String, Object> zSetOperations(RedisTemplate<String, Object> redisTemplate) {
    return redisTemplate.opsForZSet();
  }
}

