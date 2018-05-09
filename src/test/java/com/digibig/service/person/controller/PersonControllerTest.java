/*
 * Copyright 2017 - 数多科技
 * 
 * 北京数多信息科技有限公司
 * 本公司保留所有下述内容的权利。
 * 本内容为保密信息，仅限本公司内部使用。
 * 非经本公司书面许可，任何人不得外泄或用于其他目的。
 */
package com.digibig.service.person.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.digibig.service.person.PersonServiceApplicationTests;

public class PersonControllerTest extends PersonServiceApplicationTests{
  
  /**
   * <p>
   * 身份核实测试
   * </p>
   * @throws UnsupportedEncodingException
   * @throws Exception
   */
  @Test
  public void identityVerificate() throws UnsupportedEncodingException, Exception {
    
    Map<String, String> map = new HashMap<>();
    map.put("idCard", "6228271991");
    map.put("name", "李斌");

  }
  
  /**
   * <p>
   * 按身份证号查询测试
   * </p>
   * @throws UnsupportedEncodingException
   * @throws Exception
   */
  @Test
  public void getByCardNumber() throws UnsupportedEncodingException, Exception {
    
    Map<String, String> map = new HashMap<>();
    map.put("idCard", "6228271991");

  }
  
  /**
   * <p>
   * 清空首先手机号测试
   * </p>
   * @throws UnsupportedEncodingException
   * @throws Exception
   */
  @Test
  public void delMobile() throws UnsupportedEncodingException, Exception {
    
    Map<String, String> map = new HashMap<>();
    map.put("personId", "6");

  }
  
  /**
   * <p>
   * 设置首选手机号测试
   * </p>
   * @throws UnsupportedEncodingException
   * @throws Exception
   */
  @Test
  public void setMobile() throws UnsupportedEncodingException, Exception {
    
    Map<String, String> map = new HashMap<>();
    map.put("personId", "6");
    map.put("mobile", "4");

  }
  
  
  /**
   * <p>
   * 清空默认邮箱测试
   * </p>
   * @throws UnsupportedEncodingException
   * @throws Exception
   */
  @Test
  public void delEmail() throws UnsupportedEncodingException, Exception {
    
    Map<String, String> map = new HashMap<>();
    map.put("personId", "6");

  }
  
  
  /**
   * <p>
   * 设置默认邮箱测试
   * </p>
   * @throws UnsupportedEncodingException
   * @throws Exception
   */
  @Test
  public void setEmail() throws UnsupportedEncodingException, Exception {
    
    Map<String, String> map = new HashMap<>();
    map.put("personId", "6");
    map.put("emailId", "4");

  }

  /**
   * <p>
   * 清空默认地址测试
   * </p>
   * @throws UnsupportedEncodingException
   * @throws Exception
   */
  @Test
  public void remDefaultAddress() throws UnsupportedEncodingException, Exception {
    
    Map<String, String> map = new HashMap<>();
    map.put("personId", "1");

  }
  
  /**
   * <p>
   * 设置默认地址测试
   * </p>
   * @throws UnsupportedEncodingException
   * @throws Exception
   */
  @Test
  public void setDefaultAddress() throws UnsupportedEncodingException, Exception {
    
    Map<String, String> map = new HashMap<>();
    map.put("personId", "1");
    map.put("addressId", "9");

  }

  /**
   * <p>
   * 查询脱敏信息测试
   * </p>
   * @throws UnsupportedEncodingException
   * @throws Exception
   */
  @Test
  public void getDesensitizeInfo() throws UnsupportedEncodingException, Exception {

    this.mockMvc
    .perform(get("/v1.0/person/{id}/des",1))
    .andDo(print())
    .andExpect(status().isOk())
    .andReturn()
    .getResponse()
    .getContentAsString();
  }
  
  /**
   * <p>
   * 查询自然人信息测试
   * </p>
   * @throws UnsupportedEncodingException
   * @throws Exception
   */
  @Test
  public void getPersonInfo() throws UnsupportedEncodingException, Exception {
    
    Map<String, String> map = new HashMap<>();
    map.put("personId", "1");
    map.put("auth", "201");

  }
}
