package com.digibig.service.config;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
@EnableCaching // 启用缓存，这个注解很重要；
public class RedisCacheConfig extends CachingConfigurerSupport {

     /**
     * 生成key的策略
     *
     * @return
     */
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
    
    /**
     * 管理缓存
     *
     * @param redisTemplate
     * @return
     */
    @SuppressWarnings("rawtypes")
    @Bean
    public CacheManager cacheManager(RedisTemplate redisTemplate) {
        RedisCacheManager rcm = new RedisCacheManager(redisTemplate);
        //设置缓存过期时间
        // rcm.setDefaultExpiration(60);//秒
        //设置value的过期时间
        Map<String,Long> map=new HashMap<String, Long>();
        map.put("test",60L);
        rcm.setExpires(map);
        return rcm;
    }
    
//    /**
//     * RedisTemplate配置
//     * @param factory
//     * @return
//     */
//    @Bean
//    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory factory) throws UnsupportedEncodingException {
//        StringRedisTemplate template = new StringRedisTemplate(factory);
//         Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
//         ObjectMapper om = new ObjectMapper();
//         om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//         om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
//         jackson2JsonRedisSerializer.setObjectMapper(om);
//         template.setValueSerializer(jackson2JsonRedisSerializer);//如果key是String 需要配置一下StringSerializer,不然key会乱码 /XX/XX
//         template.afterPropertiesSet();
//        ValueOperations<String, String> operations = template.opsForValue();
//        Object obj = operations.get("aa");
//        System.out.println(obj.toString()+"=====================");
//         return template;
//    }

    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory factory){
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<String,String>();
        redisTemplate.setConnectionFactory(factory);
        // key序列化方式;（不然会出现乱码;）,但是如果方法上有Long等非String类型的话，会报类型转换错误；
        // 所以在没有自己定义key生成策略的时候，以下这个代码建议不要这么写，可以不配置或者自己实现ObjectRedisSerializer
        // 或者JdkSerializationRedisSerializer序列化方式;

        //接收String类型
//        RedisSerializer<String> redisSerializer = new StringRedisSerializer();// Long类型不可以会出现异常信息;
        redisTemplate.setKeySerializer(new ObjectRedisSerializer());
        redisTemplate.setValueSerializer(new ObjectRedisSerializer());
        redisTemplate.setHashKeySerializer(new ObjectRedisSerializer());
        return redisTemplate;
    }
}