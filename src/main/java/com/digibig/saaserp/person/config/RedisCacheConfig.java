/*
 * Copyright 2017 - 数多科技
 * 
 * 北京数多信息科技有限公司 本公司保留所有下述内容的权利。 本内容为保密信息，仅限本公司内部使用。 非经本公司书面许可，任何人不得外泄或用于其他目的。
 */
package com.digibig.saaserp.person.config;

import java.lang.reflect.Method;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * <p>
 * 配置redis缓存
 * </p>
 * 
 * @author zhangmingming<zhangmingming01@we.com>
 * @datetime 2017年9月6日 下午1:46:39
 * @version v1.0
 * @since 1.8
 */
@Configuration
@EnableCaching
public class RedisCacheConfig extends CachingConfigurerSupport {

  private final static Long DEFAULT_CACHE_TIME = 30 * 24 * 60 * 60L;// 默认缓存时长为30天

  @Bean
  public CacheManager cacheManager(RedisTemplate<?, ?> redisTemplate) {
    RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate);
    cacheManager.setDefaultExpiration(DEFAULT_CACHE_TIME); //
    return cacheManager;
  }

  @Bean
  public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory factory) {
    StringRedisTemplate template = new StringRedisTemplate(factory);
    setSerializer(template);
    template.afterPropertiesSet();
    return template;
  }


  @Bean
  public KeyGenerator keyGenerator() {
    return new KeyGenerator() {

      @Override
      public Object generate(Object target, Method method, Object... params) {
        StringBuilder sb = new StringBuilder();
        sb.append(target.getClass().getName());
        sb.append(method.getName());
        for (Object obj : params) {
          sb.append(obj.toString());
        }
        return sb.toString();
      }
    };
  }


  private void setSerializer(StringRedisTemplate template) {
    Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer =
        new Jackson2JsonRedisSerializer<>(Object.class);
    ObjectMapper om = new ObjectMapper();
    om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
    om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
    jackson2JsonRedisSerializer.setObjectMapper(om);
    template.setValueSerializer(jackson2JsonRedisSerializer);
  }


}
