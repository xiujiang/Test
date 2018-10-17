package com.digibig.service.config;/*
 * Copyright 2017 - 数多科技
 *
 * 北京数多信息科技有限公司 本公司保留所有下述内容的权利。
 * 本内容为保密信息，仅限本公司内部使用。
 * 非经本公司书面许可，任何人不得外泄或用于其他目的。
 */


import com.digibig.service.convert.DateConvert1;
import com.digibig.service.convert.DateFormatter1;
import com.digibig.service.interceptor.TestInterceptor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.validation.MessageCodesResolver;
import org.springframework.validation.Validator;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author liuxiujiang
 * @version 1.0
 * @datetime 2018/10/17
 * @since 1.8
 */
@Configuration
public class WebAppConfigurer implements WebMvcConfigurer {


    @Override
    public void configurePathMatch(PathMatchConfigurer pathMatchConfigurer) {
    }

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer contentNegotiationConfigurer) {

    }

    @Override
    public void configureAsyncSupport(AsyncSupportConfigurer asyncSupportConfigurer) {

    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer defaultServletHandlerConfigurer) {

    }

    @Override
    public void addFormatters(FormatterRegistry formatterRegistry) {
        System.out.println("*****转换器执行****");
        formatterRegistry.addFormatter(new DateFormatter1());
        formatterRegistry.addConverter(new DateConvert1("yyyy:MM:dd"));
        System.out.println(formatterRegistry.toString());
    }

    @Override
    public void addInterceptors(InterceptorRegistry interceptorRegistry) {
        System.out.println("启动注册。。。。");
        interceptorRegistry.addInterceptor(new TestInterceptor());
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry resourceHandlerRegistry) {

    }

    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {

    }

    @Override
    public void addViewControllers(ViewControllerRegistry viewControllerRegistry) {

    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry viewResolverRegistry) {

    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> list) {
    }

    @Override
    public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> list) {

    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> list) {
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> list) {

    }

    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> list) {

    }

    @Override
    public void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> list) {
        for (HandlerExceptionResolver handlerExceptionResolver : list) {
            if(handlerExceptionResolver instanceof MappingJackson2HttpMessageConverter){
                ObjectMapper mapper = ((MappingJackson2HttpMessageConverter) handlerExceptionResolver).getObjectMapper();
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                mapper.configure(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS, true);//这个会是什么效果？如果允许的话，会当做ordinal来处理？是否会覆盖creator
                mapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
                mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
                mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
                mapper.configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT, true);
                mapper.configure(DeserializationFeature.ACCEPT_FLOAT_AS_INT, true);
                //配置序列化参数
                mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
                mapper.configure(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS, true);


                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
                mapper.setDateFormat(simpleDateFormat);

                mapper.setLocale(Locale.ENGLISH);
                mapper.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
            }
            System.out.println("配置messageConverter:"+handlerExceptionResolver);
        }
    }

    @Override
    public Validator getValidator() {
        return null;
    }

    @Override
    public MessageCodesResolver getMessageCodesResolver() {
        return null;
    }
}
