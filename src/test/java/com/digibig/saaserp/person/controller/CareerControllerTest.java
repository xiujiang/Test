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
import com.digibig.saaserp.person.utils.Enabled;

public class CareerControllerTest extends PersonServiceApplicationTests{
  
  /**
   * 添加职业经历
   * @throws UnsupportedEncodingException
   * @throws Exception
   */
  @Test
  public void addCareer() throws UnsupportedEncodingException, Exception {
    
    Map<String, String> map = new HashMap<>();
    map.put("personId", "1");
    map.put("startDate", "2016-07-04");
    map.put("endDate", "2017-07-10");
    map.put("companyFullName", "和仁科技股份有限公司");
    map.put("companyShortName", "和仁");
    
    String addr = JSON.toJSONString(map);
    this.mockMvc
    .perform(post("/v1.0/person/career")
        .contentType(MediaType.APPLICATION_JSON)
        .content(addr))
    .andDo(print())
    .andExpect(status().isOk())
    .andReturn()
    .getResponse()
    .getContentAsString();
  }
  
  /**
   * 修改职业经历
   * @throws UnsupportedEncodingException
   * @throws Exception
   */
  @Test
  public void setCareer() throws UnsupportedEncodingException, Exception {
    
    Map<String, String> map = new HashMap<>();
    map.put("personId", "1");
    map.put("careerId", "1");
    map.put("startDate", "2016-07-04");
    map.put("endDate", "2018-07-10");
    map.put("companyFullName", "和仁科技股份有限公司");
    map.put("companyShortName", "和仁");
    
    String addr = JSON.toJSONString(map);
    this.mockMvc
    .perform(post("/v1.0/person/career/mod")
        .contentType(MediaType.APPLICATION_JSON)
        .content(addr))
    .andDo(print())
    .andExpect(status().isOk())
    .andReturn()
    .getResponse()
    .getContentAsString();
  }
  
  
  /**
   * 设置职业经历的有效性
   * @throws UnsupportedEncodingException
   * @throws Exception
   */
  @Test
  public void setCareerEnable() throws UnsupportedEncodingException, Exception {
    
    Map<String, String> map = new HashMap<>();
    map.put("personId", "1");
    map.put("careerId", "2");
    map.put("enabled", Enabled.NOT_ENABLED.toString());
    
    String addr = JSON.toJSONString(map);
    this.mockMvc
    .perform(post("/v1.0/person/career/enabled")
        .contentType(MediaType.APPLICATION_JSON)
        .content(addr))
    .andDo(print())
    .andExpect(status().isOk())
    .andReturn()
    .getResponse()
    .getContentAsString();
  }
  
  /**
   * 添加工作详情
   * @throws UnsupportedEncodingException
   * @throws Exception
   */
  @Test
  public void addCareerItem() throws UnsupportedEncodingException, Exception {
    
    Map<String, String> map = new HashMap<>();
    map.put("personId", "1");
    map.put("careerId", "2");
    map.put("department", "产品部");
    map.put("position", "软件工程师");
    map.put("startDate", "2016-07-04");
    map.put("endDate", "2017-07-10");
    map.put("description", "根据需求实现相应的功能");
    
    String addr = JSON.toJSONString(map);
    this.mockMvc
    .perform(post("/v1.0/person/career/item")
        .contentType(MediaType.APPLICATION_JSON)
        .content(addr))
    .andDo(print())
    .andExpect(status().isOk())
    .andReturn()
    .getResponse()
    .getContentAsString();
  }
  
  /**
   * 修改工作详情
   * @throws UnsupportedEncodingException
   * @throws Exception
   */
  @Test
  public void setCareerItem() throws UnsupportedEncodingException, Exception {
    
    Map<String, String> map = new HashMap<>();
    map.put("personId", "1");
    map.put("careerId", "1");
    map.put("careerItemId", "8");
    map.put("department", "研发部");
    map.put("position", "软件工程师");
    map.put("startDate", "2016-07-04");
    map.put("endDate", "2017-07-10");
    map.put("description", "根据需求实现相应的功能");
    
    String addr = JSON.toJSONString(map);
    this.mockMvc
    .perform(post("/v1.0/person/career/item/mod")
        .contentType(MediaType.APPLICATION_JSON)
        .content(addr))
    .andDo(print())
    .andExpect(status().isOk())
    .andReturn()
    .getResponse()
    .getContentAsString();
  }
  
  /**
   * 设置工作详情的有效性
   * @throws UnsupportedEncodingException
   * @throws Exception
   */
  @Test
  public void setItemEnabled() throws UnsupportedEncodingException, Exception {
    
    Map<String, String> map = new HashMap<>();
    map.put("personId", "1");
    map.put("careerId", "1");
    map.put("careerItemId", "3");
    map.put("enabled", Enabled.NOT_ENABLED.toString());
    
    String addr = JSON.toJSONString(map);
    this.mockMvc
    .perform(post("/v1.0/person/career/item/enabled")
        .contentType(MediaType.APPLICATION_JSON)
        .content(addr))
    .andDo(print())
    .andExpect(status().isOk())
    .andReturn()
    .getResponse()
    .getContentAsString();
  }
  
  /**
   * 查询自然人工作经历信息
   * @throws UnsupportedEncodingException
   * @throws Exception
   */
  @Test
  public void getCareers() throws UnsupportedEncodingException, Exception {

    this.mockMvc
    .perform(get("/v1.0/person/career/{personId}",1))
    .andDo(print())
    .andExpect(status().isOk())
    .andReturn()
    .getResponse()
    .getContentAsString();
  }
  
}
