/*
 * Copyright 2017 - 数多科技
 * 
 * 北京数多信息科技有限公司
 * 本公司保留所有下述内容的权利。
 * 本内容为保密信息，仅限本公司内部使用。
 * 非经本公司书面许可，任何人不得外泄或用于其他目的。
 */
package com.digibig.service.person.common;

public class CommonParam {
  
  private CommonParam() {
  }
  
  public static final String MAP_PARAM_EMAIL = "email";
  
  public static final String MAP_PARAM_EMAILID = "emailId";
  
  public static final String MAP_PARAM_IDCARD = "idCard";
  
  public static final String MAP_PARAM_MOBILE = "mobile";
  
  public static final String MAP_PARAM_ADDRESS = "address";
  
  public static final String MAP_PARAM_ADDRESSID = "addressId";
  
  public static final String MAP_PARAM_PERSONID = "personId";
  
  public static final String MAP_PARAM_NAME = "name";
  
  public static final String MAP_PARAM_NUMBER = "number";
  
  public static final String MAP_PARAM_ENABLED = "enabled";
  
  public static final String MAP_PARAM_ISDEFAULT = "isDefault";
  
  public static final String MAP_PARAM_AUTH = "auth";
  
  public static final String NOW = "至今";
  
  public static final String LONG_TIME = "长期";
  
  public static final String NOW_DATE = "1900-01-01";

  //脱敏时邮箱前保留的位数
  public static final Integer DEFAULT_INT = -1;
  
  //脱敏时邮箱前保留的位数
  public static final Integer EMIAL_DES_PRE = 2;
  
  //脱敏时手机号前端保留的位数
  public static final Integer MOBILE_DES_PRE = 3;
  
  //脱敏时手机号后端保留的位数
  public static final Integer MOBILE_DES_SUF = 4;
  
  //脱敏时身份证号后端保留的位数
  public static final Integer IDCARD_DES_PRE = 2;
  
  //脱敏时身份证号后端保留的位数
  public static final Integer IDCARD_DES_SUF = 4;

}
