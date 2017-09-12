/*
 * Copyright 2017 - 数多科技
 * 
 * 北京数多信息科技有限公司
 * 本公司保留所有下述内容的权利。
 * 本内容为保密信息，仅限本公司内部使用。
 * 非经本公司书面许可，任何人不得外泄或用于其他目的。
 */
package com.digibig.saaserp.person.utils;

public enum PhaseType {
  //小学
  PRIMARY_SCHOOL(1),
  //初中
  MIDDLE_SCHOOL(2),
  //高中
  HIGH_SCHOOL(3),
  //技术学院
  TECHNICAL_SCHOOL(4),
  //大学
  UNDERGRADUATE(5),
  //研究生
  GRADUATE(6),
  //博士
  DOCTOR(7),
  //博士后
  POST_DOCTOR(8),
  //培训机构
  TRAINING_AGENCY(9);
  
  private int value;
  
  private PhaseType(int value) {
    this.value = value;
  }
  
  public int getValue() {
    return this.value;
  }

}
