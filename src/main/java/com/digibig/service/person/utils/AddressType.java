/*
 * Copyright 2017 - 数多科技
 * 
 * 北京数多信息科技有限公司
 * 本公司保留所有下述内容的权利。
 * 本内容为保密信息，仅限本公司内部使用。
 * 非经本公司书面许可，任何人不得外泄或用于其他目的。
 */
package com.digibig.service.person.utils;

import org.apache.commons.lang.StringUtils;

public enum AddressType {
  
  HOME(1,"家"),
  COMPANY(2,"公司"),
  OTHER(3,"其他");
  
  private Integer value;
  private String name;
  
  private AddressType(Integer value, String name) {
    this.value = value;
    this.name = name;
  }
  
  public static String getName(int index) {
    for (AddressType privilege: AddressType.values()) {
      if (privilege.getValue() == index) {
        return privilege.getName();
      }
    }
    return null;
  }
  
  public static int getIndex(String name) {
    for (AddressType privilege: AddressType.values()) {
      if (StringUtils.equals(name, privilege.getName())) {
        return privilege.getValue();
      }
    }
    return 0;
  }
  
  public Integer getValue() {
    return this.value;
  }
  
  public String getName() {
    return this.name;
  }
}
