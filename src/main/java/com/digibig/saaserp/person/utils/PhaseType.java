/*
 * Copyright 2017 - 数多科技
 * 
 * 北京数多信息科技有限公司
 * 本公司保留所有下述内容的权利。
 * 本内容为保密信息，仅限本公司内部使用。
 * 非经本公司书面许可，任何人不得外泄或用于其他目的。
 */
package com.digibig.saaserp.person.utils;

import org.apache.commons.lang.StringUtils;

public enum PhaseType {
  //小学
  PRIMARY_SCHOOL(1,"小学"),
  //初中
  MIDDLE_SCHOOL(2,"初中"),
  //高中
  HIGH_SCHOOL(3,"高中"),
  //技术学院
  TECHNICAL_SCHOOL(4,"技术学院"),
  //大学
  UNDERGRADUATE(5,"大学"),
  //研究生
  GRADUATE(6,"研究生"),
  //博士
  DOCTOR(7,"博士"),
  //博士后
  POST_DOCTOR(8,"博士后"),
  //培训机构
  TRAINING_AGENCY(9,"培训机构");
  
  private int value;
  private String name;
  
  private PhaseType(int value, String name) {
    this.value = value;
    this.name= name;
  }
  
  public static String getName(Integer index) {
    for (PhaseType privilege: PhaseType.values()) {
      if (privilege.getValue() == index) {
        return privilege.getName();
      }
    }
    return null;
  }
  
  public static Integer getValue(String name) {
    for (PhaseType privilege: PhaseType.values()) {
      if (StringUtils.equals(name, privilege.getName())) {
        return privilege.getValue();
      }
    }
    return 0;
  }
  
  public int getValue() {
    return this.value;
  }
  
  public String getName() {
    return this.name;
  }

}
