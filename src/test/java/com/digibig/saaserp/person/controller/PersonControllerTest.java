/*
 * Copyright 2017 - 数多科技
 * 
 * 北京数多信息科技有限公司
 * 本公司保留所有下述内容的权利。
 * 本内容为保密信息，仅限本公司内部使用。
 * 非经本公司书面许可，任何人不得外泄或用于其他目的。
 */
package com.digibig.saaserp.person.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.springframework.http.MediaType;

import com.alibaba.fastjson.JSON;
import com.digibig.saaserp.person.PersonServiceApplicationTests;

public class PersonControllerTest extends PersonServiceApplicationTests{
  @Test
  public void identityVerificate() throws UnsupportedEncodingException, Exception {
    
    Map<String, String> map = new HashMap<>();
    map.put("IDCard", "622827199112050611");
    map.put("name", "李斌");
    
    String addr = JSON.toJSONString(map);
    this.mockMvc
    .perform(post("/v1.0/person/veri")
        .contentType(MediaType.APPLICATION_JSON)
        .content(addr))
    .andDo(print())
    .andExpect(status().isOk())
    .andReturn()
    .getResponse()
    .getContentAsString();
  }
  
  @Test
  public void getByCardNumber() throws UnsupportedEncodingException, Exception {
    
    Map<String, String> map = new HashMap<>();
    map.put("IDCard", "622827199112050611");
    
    String addr = JSON.toJSONString(map);
    this.mockMvc
    .perform(post("/v1.0/person/cardno")
        .contentType(MediaType.APPLICATION_JSON)
        .content(addr))
    .andDo(print())
    .andExpect(status().isOk())
    .andReturn()
    .getResponse()
    .getContentAsString();
  }
  
  @Test
  public void setMobile() throws UnsupportedEncodingException, Exception {
    
    Map<String, String> map = new HashMap<>();
    map.put("personId", "1");
    map.put("mobile", "1");
    
    String addr = JSON.toJSONString(map);
    this.mockMvc
    .perform(post("/v1.0/person/mobile/default")
        .contentType(MediaType.APPLICATION_JSON)
        .content(addr))
    .andDo(print())
    .andExpect(status().isOk())
    .andReturn()
    .getResponse()
    .getContentAsString();
  }
  
  @Test
  public void delMobile() throws UnsupportedEncodingException, Exception {
    
    Map<String, String> map = new HashMap<>();
    map.put("personId", "1");
    map.put("startDate", "2016-07-04");
    map.put("endDate", "2017-07-10");
    map.put("companyFullName", "和仁科技股份有限公司");
    map.put("companyShortName", "和仁");
    
    String addr = JSON.toJSONString(map);
    this.mockMvc
    .perform(post("/v1.0/person/mobile/default/rem")
        .contentType(MediaType.APPLICATION_JSON)
        .content(addr))
    .andDo(print())
    .andExpect(status().isOk())
    .andReturn()
    .getResponse()
    .getContentAsString();
  }
  
  @Test
  public void setEmail() throws UnsupportedEncodingException, Exception {
    
    Map<String, String> map = new HashMap<>();
    map.put("personId", "1");
    map.put("startDate", "2016-07-04");
    map.put("endDate", "2017-07-10");
    map.put("companyFullName", "和仁科技股份有限公司");
    map.put("companyShortName", "和仁");
    
    String addr = JSON.toJSONString(map);
    this.mockMvc
    .perform(post("/v1.0/person/email/default")
        .contentType(MediaType.APPLICATION_JSON)
        .content(addr))
    .andDo(print())
    .andExpect(status().isOk())
    .andReturn()
    .getResponse()
    .getContentAsString();
  }
  
  @Test
  public void delEmail() throws UnsupportedEncodingException, Exception {
    
    Map<String, String> map = new HashMap<>();
    map.put("personId", "1");
    map.put("startDate", "2016-07-04");
    map.put("endDate", "2017-07-10");
    map.put("companyFullName", "和仁科技股份有限公司");
    map.put("companyShortName", "和仁");
    
    String addr = JSON.toJSONString(map);
    this.mockMvc
    .perform(post("/v1.0/person/email/default/rem")
        .contentType(MediaType.APPLICATION_JSON)
        .content(addr))
    .andDo(print())
    .andExpect(status().isOk())
    .andReturn()
    .getResponse()
    .getContentAsString();
  }
  
  @Test
  public void setDefaultAddress() throws UnsupportedEncodingException, Exception {
    
    Map<String, String> map = new HashMap<>();
    map.put("personId", "1");
    map.put("startDate", "2016-07-04");
    map.put("endDate", "2017-07-10");
    map.put("companyFullName", "和仁科技股份有限公司");
    map.put("companyShortName", "和仁");
    
    String addr = JSON.toJSONString(map);
    this.mockMvc
    .perform(post("/v1.0/person/address/default")
        .contentType(MediaType.APPLICATION_JSON)
        .content(addr))
    .andDo(print())
    .andExpect(status().isOk())
    .andReturn()
    .getResponse()
    .getContentAsString();
  }
  
  @Test
  public void remDefaultAddress() throws UnsupportedEncodingException, Exception {
    
    Map<String, String> map = new HashMap<>();
    map.put("personId", "1");
    map.put("startDate", "2016-07-04");
    map.put("endDate", "2017-07-10");
    map.put("companyFullName", "和仁科技股份有限公司");
    map.put("companyShortName", "和仁");
    
    String addr = JSON.toJSONString(map);
    this.mockMvc
    .perform(post("/v1.0/person/address/default/rem")
        .contentType(MediaType.APPLICATION_JSON)
        .content(addr))
    .andDo(print())
    .andExpect(status().isOk())
    .andReturn()
    .getResponse()
    .getContentAsString();
  }
  
  @Test
  public void getDesensitizeInfo() throws UnsupportedEncodingException, Exception {
    
    Map<String, String> map = new HashMap<>();
    map.put("personId", "1");
    map.put("startDate", "2016-07-04");
    map.put("endDate", "2017-07-10");
    map.put("companyFullName", "和仁科技股份有限公司");
    map.put("companyShortName", "和仁");
    
    String addr = JSON.toJSONString(map);
    this.mockMvc
    .perform(get("/v1.0/person/{id}/des")
        .contentType(MediaType.APPLICATION_JSON)
        .content(addr))
    .andDo(print())
    .andExpect(status().isOk())
    .andReturn()
    .getResponse()
    .getContentAsString();
  }
  
  @Test
  public void getPersonInfo() throws UnsupportedEncodingException, Exception {
    
    Map<String, String> map = new HashMap<>();
    map.put("personId", "1");
    map.put("startDate", "2016-07-04");
    map.put("endDate", "2017-07-10");
    map.put("companyFullName", "和仁科技股份有限公司");
    map.put("companyShortName", "和仁");
    
    String addr = JSON.toJSONString(map);
    this.mockMvc
    .perform(post("/v1.0/person/list")
        .contentType(MediaType.APPLICATION_JSON)
        .content(addr))
    .andDo(print())
    .andExpect(status().isOk())
    .andReturn()
    .getResponse()
    .getContentAsString();
  }
}
