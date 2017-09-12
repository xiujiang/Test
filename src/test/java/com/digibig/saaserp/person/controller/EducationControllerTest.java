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
import com.digibig.saaserp.person.utils.DegreeGetType;
import com.digibig.saaserp.person.utils.Enabled;
import com.digibig.saaserp.person.utils.PhaseType;
import com.digibig.saaserp.person.utils.VarificationStatus;

public class EducationControllerTest extends PersonServiceApplicationTests{
  
  @Test
  public void addEducation() throws UnsupportedEncodingException, Exception {
    
    Map<String, String> map = new HashMap<>();
    map.put("personId", "1");
    map.put("startDate", "2012-09-03");
    map.put("endDate", "2016-06-10");
    map.put("schoolName", "北京理工大学");
    map.put("faculty", "机械与车辆学院");
    map.put("profession", "交通工程");
    map.put("type", DegreeGetType.NATIONAL_UNIFIED_ENTRANCE_EXAMINATION.toString());
    map.put("phase", PhaseType.UNDERGRADUATE.toString());
    map.put("diplomaId", "1");
    map.put("recordId", "1");
    
    String addr = JSON.toJSONString(map);
    this.mockMvc
    .perform(post("/v1.0/person/education")
        .contentType(MediaType.APPLICATION_JSON)
        .content(addr))
    .andDo(print())
    .andExpect(status().isOk())
    .andReturn()
    .getResponse()
    .getContentAsString();
  }
  
  
  @Test
  public void setEducation() throws UnsupportedEncodingException, Exception {
    
    Map<String, String> map = new HashMap<>();
    map.put("personId", "1");
    map.put("educationId", "1");
    map.put("startDate", "2012-09-04");
    map.put("endDate", "2016-06-10");
    map.put("schoolName", "北京理工大学");
    map.put("faculty", "机械与车辆学院");
    map.put("profession", "交通工程");
    map.put("type", DegreeGetType.NATIONAL_UNIFIED_ENTRANCE_EXAMINATION.toString());
    map.put("phase", PhaseType.UNDERGRADUATE.toString());
    map.put("diplomaId", "1");
    map.put("recordId", "1");
    
    String addr = JSON.toJSONString(map);
    this.mockMvc
    .perform(post("/v1.0/person/education/mod")
        .contentType(MediaType.APPLICATION_JSON)
        .content(addr))
    .andDo(print())
    .andExpect(status().isOk())
    .andReturn()
    .getResponse()
    .getContentAsString();
  }
  
  
  @Test
  public void setEducationEnabled() throws UnsupportedEncodingException, Exception {
    
    Map<String, String> map = new HashMap<>();
    map.put("personId", "1");
    map.put("educationId", "1");
    map.put("enabled", Enabled.ENABLED.toString());
    
    String addr = JSON.toJSONString(map);
    this.mockMvc
    .perform(post("/v1.0/person/education/enabled")
        .contentType(MediaType.APPLICATION_JSON)
        .content(addr))
    .andDo(print())
    .andExpect(status().isOk())
    .andReturn()
    .getResponse()
    .getContentAsString();
  }
  
  
  @Test
  public void setEducationSummary() throws UnsupportedEncodingException, Exception {
    
    Map<String, String> map = new HashMap<>();
    map.put("personId", "1");
    map.put("lastDegree", "学士学位");
    map.put("bDegreeIssuer", "北京理工大学");
    map.put("bDegreeProfession", "计算机");
    map.put("bDegreeType", DegreeGetType.NATIONAL_UNIFIED_ENTRANCE_EXAMINATION.toString());
    map.put("bDegreeYear", "2016");
    map.put("bDegreeRefId", "1");
    map.put("bDegreeVerification", VarificationStatus.AUTHORIZATION.toString());
    map.put("mDegreeIssuer", "北京理工大学");
    map.put("mDegreeProfession", "计算机");
    map.put("mDegreeType", DegreeGetType.NATIONAL_UNIFIED_ENTRANCE_EXAMINATION.toString());
    map.put("mDegreeYear", "2018");
    map.put("mDegreeRefId", "2");
    map.put("mDegreeVerification", VarificationStatus.AUTHORIZATION.toString());
    
    String addr = JSON.toJSONString(map);
    this.mockMvc
    .perform(post("/v1.0/person/education/summary")
        .contentType(MediaType.APPLICATION_JSON)
        .content(addr))
    .andDo(print())
    .andExpect(status().isOk())
    .andReturn()
    .getResponse()
    .getContentAsString();
  }
}
