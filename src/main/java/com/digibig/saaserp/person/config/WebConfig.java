/*
 * Copyright 2017 - 数多科技
 * 
 * 北京数多信息科技有限公司 本公司保留所有下述内容的权利。 本内容为保密信息，仅限本公司内部使用。 非经本公司书面许可，任何人不得外泄或用于其他目的。
 */
package com.digibig.saaserp.person.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * <p>
 * 修改spring mvc默认返回实现，去掉返回中为null的字段
 * </p>
 * 
 * @author zhangmingming<zhangmingming01@we.com>
 * @datetime 2017年8月22日 下午3:17:54
 * @version v1.0
 * @since 1.8
 */
@Configuration
public class WebConfig extends WebMvcConfigurationSupport {

  @Bean
  public MappingJackson2HttpMessageConverter customJackson2HttpMessageConverter() {
    MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.setSerializationInclusion(Include.NON_NULL);
    jsonConverter.setObjectMapper(objectMapper);
    return jsonConverter;
  }

  @Override
  protected void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
    converters.add(customJackson2HttpMessageConverter());
    super.configureMessageConverters(converters);
  }



}
