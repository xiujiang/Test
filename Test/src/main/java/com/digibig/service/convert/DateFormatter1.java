package com.digibig.service.convert;/*
 * Copyright 2017 - 数多科技
 *
 * 北京数多信息科技有限公司 本公司保留所有下述内容的权利。
 * 本内容为保密信息，仅限本公司内部使用。
 * 非经本公司书面许可，任何人不得外泄或用于其他目的。
 */


import org.springframework.context.annotation.Configuration;
import org.springframework.format.Formatter;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author liuxiujiang
 * @version 1.0
 * @datetime 2018/10/17
 * @since 1.8
 */
public class DateFormatter1 implements Formatter<Date> {
    @Override
    public Date parse(String text, Locale locale) throws ParseException {
        if(StringUtils.isEmpty(text)){
            return null;
        }
        System.out.println("11123123123123");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy:MM:dd");
        return simpleDateFormat.parse(text);
    }

    @Override
    public String print(Date object, Locale locale) {
        return object.toString();
    }
}
