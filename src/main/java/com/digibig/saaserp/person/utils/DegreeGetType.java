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

public enum DegreeGetType {
  
   NATIONAL_UNIFIED_ENTRANCE_EXAMINATION(1,"全国统招"),
   ADULT_CONTAINS(2,"成人自考");
  
  private Integer value;
  private String name;
    
  private DegreeGetType(Integer value, String name) {
    this.value = value;
    this.name = name;
  }
  
  public static String getName(Integer index) {
    for (DegreeGetType privilege: DegreeGetType.values()) {
      if (privilege.getValue() == index) {
        return privilege.getName();
      }
    }
    return null;
  }
  
  public static Integer getIndex(String name) {
    for (DegreeGetType privilege: DegreeGetType.values()) {
      if (StringUtils.equals(name, privilege.getName())) {
        return privilege.getValue();
      }
    }
    return 0;
  }
  
  public String getName() {
    return this.name;
  }
  
  public Integer getValue() {
    return this.value;
  }

}
