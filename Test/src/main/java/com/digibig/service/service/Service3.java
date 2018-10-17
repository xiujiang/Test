package com.digibig.service.service;/*
 * Copyright 2017 - 数多科技
 *
 * 北京数多信息科技有限公司 本公司保留所有下述内容的权利。
 * 本内容为保密信息，仅限本公司内部使用。
 * 非经本公司书面许可，任何人不得外泄或用于其他目的。
 */


import java.util.List;

/**
 * @author liuxiujiang
 * @version 1.0
 * @datetime 2018/10/8
 * @since 1.8
 */
public class Service3<DATA> {

    private Service3.LoadFunction<DATA> loadFunction;

    Service3(LoadFunction<DATA> loadFunction){
        this.loadFunction= loadFunction;
    }

    public interface LoadFunction<D> {
        List<D> load(List<D> var1);
    }

    private List<DATA> load(List<DATA> current) {
        return this.loadFunction.load(current);
    }

}
