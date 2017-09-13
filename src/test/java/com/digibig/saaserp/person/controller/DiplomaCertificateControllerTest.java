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
import com.digibig.saaserp.person.utils.DegreeGetType;
import com.digibig.saaserp.person.utils.DiplomaType;
import com.digibig.saaserp.person.utils.Enabled;
import com.digibig.saaserp.person.utils.PhaseType;
import com.digibig.saaserp.person.utils.SchoolResult;
import com.digibig.saaserp.person.utils.VarificationStatus;

public class DiplomaCertificateControllerTest extends PersonServiceApplicationTests{
  
  
  /**
   * <p>
   * 添加学位证测试
   * </p>
   * @throws UnsupportedEncodingException
   * @throws Exception
   */
  @Test
  public void addDiplomaCertificate() throws UnsupportedEncodingException, Exception {
    
    Map<String, String> map = new HashMap<>();
    map.put("personId", "1");
    map.put("certificateNumber", "56254855454");
    map.put("diploma", "学士学位");
    map.put("issuer", "北京理工大学");
    map.put("profession", "交通工程");
    map.put("date", "2016-06-04");
    map.put("diplomaType", DiplomaType.BACHELOR.toString());
    map.put("type", DegreeGetType.NATIONAL_UNIFIED_ENTRANCE_EXAMINATION.toString());
    map.put("varificationStatus", VarificationStatus.AUTHORIZATION.toString());
    map.put("file", "11");
    map.put("varificationFile", "13");
    
    String addr = JSON.toJSONString(map);
    this.mockMvc
    .perform(post("/v1.0/person/certificate/diploma")
        .contentType(MediaType.APPLICATION_JSON)
        .content(addr))
    .andDo(print())
    .andExpect(status().isOk())
    .andReturn()
    .getResponse()
    .getContentAsString();
  }
  
  
  /**
   * <p>
   * 修改学位证测试
   * </p>
   * @throws UnsupportedEncodingException
   * @throws Exception
   */
  @Test
  public void setDiplomaCertificate() throws UnsupportedEncodingException, Exception {
    
    Map<String, String> map = new HashMap<>();
    map.put("personId", "1");
    map.put("id", "1");
    map.put("certificateNumber", "56254855454");
    map.put("diploma", "学士学位");
    map.put("issuer", "北京理工大学");
    map.put("profession", "交通工程");
    map.put("date", "2016-06-04");
    map.put("diplomaType", DiplomaType.BACHELOR.toString());
    map.put("type", DegreeGetType.NATIONAL_UNIFIED_ENTRANCE_EXAMINATION.toString());
    map.put("varificationStatus", VarificationStatus.AUTHORIZATION.toString());
    map.put("file", "11");
    map.put("varificationFile", "10");
    
    String addr = JSON.toJSONString(map);
    this.mockMvc
    .perform(post("/v1.0/person/certificate/diploma/mod")
        .contentType(MediaType.APPLICATION_JSON)
        .content(addr))
    .andDo(print())
    .andExpect(status().isOk())
    .andReturn()
    .getResponse()
    .getContentAsString();
  }
  
  
  /**
   * <p>
   * 设置学位证有效性测试
   * </p>
   * @throws UnsupportedEncodingException
   * @throws Exception
   */
  @Test
  public void setCertificateEnabled() throws UnsupportedEncodingException, Exception {
    
    Map<String, String> map = new HashMap<>();
    map.put("personId", "6");
    map.put("certificateId", "2");
    map.put("enabled", Enabled.ENABLED.toString());
    
    String addr = JSON.toJSONString(map);
    this.mockMvc
    .perform(post("/v1.0/person/certificate/diploma/enabled")
        .contentType(MediaType.APPLICATION_JSON)
        .content(addr))
    .andDo(print())
    .andExpect(status().isOk())
    .andReturn()
    .getResponse()
    .getContentAsString();
  }
  
  
  /**
   * <p>
   * 添加学历证测试
   * </p>
   * @throws UnsupportedEncodingException
   * @throws Exception
   */
  @Test
  public void addSchoolRecord() throws UnsupportedEncodingException, Exception {
    
    Map<String, String> map = new HashMap<>();
    map.put("personId", "1");
    map.put("certificateNumber", "556552452");
    map.put("issuer", "北京理工大学");
    map.put("profession", "计算机");
    map.put("result", SchoolResult.GRADUATION.toString());
    map.put("type", DegreeGetType.NATIONAL_UNIFIED_ENTRANCE_EXAMINATION.toString());
    map.put("phase", PhaseType.UNDERGRADUATE.toString());
    map.put("date", "2016-06-10");
    map.put("file", "55");
    map.put("verificationStatus", VarificationStatus.AUTHORIZATION.toString());
    map.put("verificationFile", "56");
    
    String addr = JSON.toJSONString(map);
    this.mockMvc
    .perform(post("/v1.0/person/certificate/school")
        .contentType(MediaType.APPLICATION_JSON)
        .content(addr))
    .andDo(print())
    .andExpect(status().isOk())
    .andReturn()
    .getResponse()
    .getContentAsString();
  }
  
  
  /**
   * <p>
   * 修改学历证测试
   * </p>
   * @throws UnsupportedEncodingException
   * @throws Exception
   */
  @Test
  public void setSchoolRecord() throws UnsupportedEncodingException, Exception {
    
    Map<String, String> map = new HashMap<>();
    map.put("personId", "1");
    map.put("id", "1");
    map.put("certificateNumber", "556552452");
    map.put("issuer", "北京理工大学");
    map.put("profession", "计算机科学与技术");
    map.put("result", SchoolResult.GRADUATION.toString());
    map.put("type", DegreeGetType.NATIONAL_UNIFIED_ENTRANCE_EXAMINATION.toString());
    map.put("phase", PhaseType.UNDERGRADUATE.toString());
    map.put("date", "2016-06-10");
    map.put("file", "55");
    map.put("verificationStatus", VarificationStatus.AUTHORIZATION.toString());
    map.put("verificationFile", "56");
    
    String addr = JSON.toJSONString(map);
    this.mockMvc
    .perform(post("/v1.0/person/certificate/school/mod")
        .contentType(MediaType.APPLICATION_JSON)
        .content(addr))
    .andDo(print())
    .andExpect(status().isOk())
    .andReturn()
    .getResponse()
    .getContentAsString();
  }
  
  
  /**
   * <p>
   * 设置学历证有效性测试
   * </p>
   * @throws UnsupportedEncodingException
   * @throws Exception
   */
  @Test
  public void setRecordEnabled() throws UnsupportedEncodingException, Exception {
    
    Map<String, String> map = new HashMap<>();
    map.put("personId", "1");
    map.put("recordId", "1");
    map.put("enabled", Enabled.ENABLED.toString());
    
    String addr = JSON.toJSONString(map);
    this.mockMvc
    .perform(post("/v1.0/person/certificate/school/enabled")
        .contentType(MediaType.APPLICATION_JSON)
        .content(addr))
    .andDo(print())
    .andExpect(status().isOk())
    .andReturn()
    .getResponse()
    .getContentAsString();
  }
  
  
  /**
   * <p>
   * 添加学信网学历证书测试
   * </p>
   * @throws UnsupportedEncodingException
   * @throws Exception
   */
  @Test
  public void addCheckReport() throws UnsupportedEncodingException, Exception {
    
    Map<String, String> map = new HashMap<>();
    map.put("personId", "1");
    map.put("recordId", "1");
    map.put("reportFile", "44");
    
    String addr = JSON.toJSONString(map);
    this.mockMvc
    .perform(post("/v1.0/person/certificate/school/veri")
        .contentType(MediaType.APPLICATION_JSON)
        .content(addr))
    .andDo(print())
    .andExpect(status().isOk())
    .andReturn()
    .getResponse()
    .getContentAsString();
  }
  
  
  /**
   * <p>
   * 获取学历证信息
   * </p>
   * @throws UnsupportedEncodingException
   * @throws Exception
   */
  @Test
  public void getSchoolRecord() throws UnsupportedEncodingException, Exception {
    
    this.mockMvc
    .perform(get("/v1.0/person/certificate/{personId}",1))
    .andDo(print())
    .andExpect(status().isOk())
    .andReturn()
    .getResponse()
    .getContentAsString();
  }
}
