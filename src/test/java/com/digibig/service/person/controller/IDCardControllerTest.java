/*
 * Copyright 2017 - 数多科技
 * 
 * 北京数多信息科技有限公司
 * 本公司保留所有下述内容的权利。
 * 本内容为保密信息，仅限本公司内部使用。
 * 非经本公司书面许可，任何人不得外泄或用于其他目的。
 */
package com.digibig.service.person.controller;

import com.digibig.service.person.PersonServiceApplicationTests;
import com.digibig.service.person.enums.Gender;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

public class IDCardControllerTest extends PersonServiceApplicationTests{
  
  
  /**
   * <p>
   * 绑定身份证 测试
   * </p>
   * @throws UnsupportedEncodingException
   * @throws Exception
   */
  @Test
  public void addIdCard() throws UnsupportedEncodingException, Exception {
    
    Map<String, String> map = new HashMap<>();
    map.put("personId", "1");
    map.put("name", "李斌");
    map.put("number", "622827199112050611");
    map.put("issueDate", "2011-07-04");
    map.put("expire", "2026-07-04");
    map.put("address", "甘肃省");
    map.put("pGender", Gender.MAN.toString());
    map.put("agency", "平泉派出所");
    map.put("frontPicture", "122");
    map.put("backPicture", "150");
    map.put("isDefault", "true");

  }
  
  
  /**
   * <p>
   * 设置身份证照片 测试
   * </p>
   * @throws UnsupportedEncodingException
   * @throws Exception
   */
  @Test
  public void setCardPicture() throws UnsupportedEncodingException, Exception {
    
    Map<String, String> map = new HashMap<>();
    map.put("personId", "1");
    map.put("idCardId", "1");
    map.put("frontPic", "100");
    map.put("backPic", "101");

  }
}
