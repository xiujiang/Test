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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.digibig.service.person.PersonServiceApplicationTests;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

public class CareerControllerTest extends PersonServiceApplicationTests{
  
  /**
   * <p>
   * 添加职业经历
   * </p>
   * @throws UnsupportedEncodingException
   * @throws Exception
   */
  @Test
  public void addCareer() throws UnsupportedEncodingException, Exception {
    
    Map<String, String> map = new HashMap<>();
    map.put("personId", "1");
    map.put("startDate", "2016-07-04");
    map.put("end", "2017-07-10");
    map.put("companyFullName", "和仁科技股份有限公司");
    map.put("companyShortName", "和仁");

  }
  
  /**
   * <p>
   * 修改职业经历
   * </p>
   * @throws UnsupportedEncodingException
   * @throws Exception
   */
  @Test
  public void setCareer() throws UnsupportedEncodingException, Exception {
    
    Map<String, String> map = new HashMap<>();
    map.put("personId", "1");
    map.put("id", "1");
    map.put("startDate", "2016-07-04");
    map.put("end", "2018-07-10");
    map.put("companyFullName", "和仁科技股份有限公司");
    map.put("companyShortName", "和仁");

  }
  
  
  /**
   * <p>
   * 设置职业经历的有效性
   * </p>
   * @throws UnsupportedEncodingException
   * @throws Exception
   */
  @Test
  public void setCareerEnable() throws UnsupportedEncodingException, Exception {
    
    Map<String, String> map = new HashMap<>();
    map.put("personId", "1");
    map.put("careerId", "2");
//    map.put("enabled", Enabled.NOT_ENABLED.toString());

  }
  
  /**
   * <p>
   * 添加工作详情
   * </p>
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
    map.put("end", "2017-07-10");
    map.put("description", "根据需求实现相应的功能");

  }
  
  /**<p>
   * 修改工作详情
   * </p>
   * @throws UnsupportedEncodingException
   * @throws Exception
   */
  @Test
  public void setCareerItem() throws UnsupportedEncodingException, Exception {
    
    Map<String, String> map = new HashMap<>();
    map.put("personId", "1");
    map.put("careerId", "1");
    map.put("id", "8");
    map.put("department", "研发部");
    map.put("position", "软件工程师");
    map.put("startDate", "2016-07-04");
    map.put("end", "2017-07-07");
    map.put("description", "根据需求实现相应的功能");

  }
  
  /**
   * <p>
   * 设置工作详情的有效性
   * </p>
   * @throws UnsupportedEncodingException
   * @throws Exception
   */
  @Test
  public void setItemEnabled() throws UnsupportedEncodingException, Exception {
    
    Map<String, String> map = new HashMap<>();
    map.put("personId", "1");
    map.put("careerId", "1");
    map.put("careerItemId", "3");
//    map.put("enabled", Enabled.NOT_ENABLED.toString());

  }
  
  /**
   * <p>
   * 查询自然人工作经历信息
   * </p>
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
