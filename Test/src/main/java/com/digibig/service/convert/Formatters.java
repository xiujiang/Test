package com.digibig.service.convert;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.format.support.DefaultFormattingConversionService;

@Configuration
public class Formatters {
    @Autowired
    public Formatters(@Qualifier("mvcConversionService") ConversionService conversionService) {
        DefaultFormattingConversionService service = (DefaultFormattingConversionService) conversionService;
        service.addConverter(new DateConvert1("yyyy:MM:dd"));
        System.out.println(service.toString());
        System.out.println("Formatter DateConvert1");
    }
}