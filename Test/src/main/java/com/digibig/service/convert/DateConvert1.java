package com.digibig.service.convert;/*
 * Copyright 2017 - 数多科技
 *
 * 北京数多信息科技有限公司 本公司保留所有下述内容的权利。
 * 本内容为保密信息，仅限本公司内部使用。
 * 非经本公司书面许可，任何人不得外泄或用于其他目的。
 */


import org.springframework.cglib.core.Converter;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author liuxiujiang
 * @version 1.0
 * @datetime 2018/10/17
 * @since 1.8
 */
public class DateConvert1 implements GenericConverter {

    private String pattern;
    public  DateConvert1(String pattern){
        this.pattern = pattern;
    }
    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        HashSet<ConvertiblePair> canConvertSet=new HashSet<>();
        canConvertSet.add(new ConvertiblePair(String.class, Date.class));
        return canConvertSet;
    }

    @Override
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        if(source == null){
            return null;
        }
        if(source instanceof String){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            try {
                return simpleDateFormat.parse(source.toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return "123123";
    }
}
