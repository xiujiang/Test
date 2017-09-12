/*
 * Copyright 2017 - 数多科技
 * 
 * 北京数多信息科技有限公司
 * 本公司保留所有下述内容的权利。
 * 本内容为保密信息，仅限本公司内部使用。
 * 非经本公司书面许可，任何人不得外泄或用于其他目的。
 */
package com.digibig.saaserp.person.controller;

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
import com.digibig.saaserp.person.utils.Enabled;

public class MobileControllerTest extends PersonServiceApplicationTests{
  
  @Test
  public void addMobile() throws UnsupportedEncodingException, Exception {
    
    Map<String, String> map = new HashMap<>();
    map.put("personId", "1");
    map.put("mobileNumber", "13755655525");
    map.put("isDefault", "true");
    
    String addr = JSON.toJSONString(map);
    this.mockMvc
    .perform(post("/v1.0/person/mobiles")
        .contentType(MediaType.APPLICATION_JSON)
        .content(addr))
    .andDo(print())
    .andExpect(status().isOk())
    .andReturn()
    .getResponse()
    .getContentAsString();
  }
  
  @Test
  public void setMobileEnabled() throws UnsupportedEncodingException, Exception {
    
    Map<String, String> map = new HashMap<>();
    map.put("personId", "1");
    map.put("mobile", "3");
    map.put("enabled", Enabled.ENABLED.toString());
    
    String addr = JSON.toJSONString(map);
    this.mockMvc
    .perform(post("/v1.0/person/mobiles/enabled")
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
    
    String addr = JSON.toJSONString(map);
    this.mockMvc
    .perform(post("/v1.0/person/mobiles/list/des")
        .contentType(MediaType.APPLICATION_JSON)
        .content(addr))
    .andDo(print())
    .andExpect(status().isOk())
    .andReturn()
    .getResponse()
    .getContentAsString();
  }
  
  @Test
  public void getMobileInfo() throws UnsupportedEncodingException, Exception {
    
    Map<String, String> map = new HashMap<>();
    map.put("personId", "1");
    map.put("auth", "2016-07-04");
    
    String addr = JSON.toJSONString(map);
    this.mockMvc
    .perform(post("/v1.0/person/mobiles/list")
        .contentType(MediaType.APPLICATION_JSON)
        .content(addr))
    .andDo(print())
    .andExpect(status().isOk())
    .andReturn()
    .getResponse()
    .getContentAsString();
  }
}
