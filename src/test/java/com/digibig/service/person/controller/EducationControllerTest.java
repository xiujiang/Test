/*
 * Copyright 2017 - 数多科技
 * 
 * 北京数多信息科技有限公司
 * 本公司保留所有下述内容的权利。
 * 本内容为保密信息，仅限本公司内部使用。
 * 非经本公司书面许可，任何人不得外泄或用于其他目的。
 */
package com.digibig.service.person.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.digibig.service.person.PersonServiceApplicationTests;
import com.digibig.service.person.utils.DegreeGetType;
import com.digibig.service.person.utils.Enabled;
import com.digibig.service.person.utils.PhaseType;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.springframework.http.MediaType;

import com.digibig.service.person.utils.VerificationStatus;

public class EducationControllerTest extends PersonServiceApplicationTests {
  
  
  /**
   * <p>
   * 添加教育经历测试
   * </p>
   * @throws UnsupportedEncodingException
   * @throws Exception
   */
  @Test
  public void addEducation() throws UnsupportedEncodingException, Exception {
    
    Map<String, String> map = new HashMap<>();
    map.put("personId", "1");
    map.put("startDate", "2012-09-03");
    map.put("end", "2016-06-10");
    map.put("schoolName", "北京理工大学");
    map.put("faculty", "机械与车辆学院");
    map.put("profession", "交通工程");
    map.put("type", DegreeGetType.NATIONAL_UNIFIED_ENTRANCE_EXAMINATION.toString());
    map.put("phase", PhaseType.UNDERGRADUATE.toString());
    map.put("diplomaId", "1");
    map.put("recordId", "1");

  }
  
  
  /**
   * <p>
   * 修改教育经历测试
   * </p>
   * @throws UnsupportedEncodingException
   * @throws Exception
   */
  @Test
  public void setEducation() throws UnsupportedEncodingException, Exception {
    
    Map<String, String> map = new HashMap<>();
    map.put("personId", "1");
    map.put("id", "6");
    map.put("startDate", "2012-09-04");
    map.put("end", "2016-06-10");
    map.put("schoolName", "北京理工大学");
    map.put("faculty", "机械与车辆学院");
    map.put("profession", "交通工程");
    map.put("type", DegreeGetType.NATIONAL_UNIFIED_ENTRANCE_EXAMINATION.toString());
    map.put("phase", PhaseType.UNDERGRADUATE.toString());
    map.put("diplomaId", "122");
    map.put("recordId", "133");

  }
  
  
  /**
   * <p>
   * 设置教育经历有效性测试
   * </p>
   * @throws UnsupportedEncodingException
   * @throws Exception
   */
  @Test
  public void setEducationEnabled() throws UnsupportedEncodingException, Exception {
    
    Map<String, String> map = new HashMap<>();
    map.put("personId", "1");
    map.put("educationId", "1");
    map.put("enabled", Enabled.ENABLED.toString());

  }
  
  
  /**
   * <p>
   * 设置教育摘要测试
   * </p>
   * @throws UnsupportedEncodingException
   * @throws Exception
   */
  @Test
  public void setEducationSummary() throws UnsupportedEncodingException, Exception {
    
    Map<String, String> map = new HashMap<>();
    map.put("personId", "1");
    map.put("lastDegree", "学士");
    map.put("bachelorDegreeIssuer", "北京理工大学");
    map.put("bachelorDegreeProfession", "计算机");
    map.put("bachelorDegreeType", DegreeGetType.NATIONAL_UNIFIED_ENTRANCE_EXAMINATION.toString());
    map.put("bachelorDegreeYear", "2016");
    map.put("bachelorDegreeRefId", "1");
    map.put("bachelorDegreeVerification", VerificationStatus.AUTHORIZATION.toString());
    map.put("masterDegreeIssuer", "北京理工大学");
    map.put("masterDegreeProfession", "计算机");
    map.put("masterDegreeType", DegreeGetType.NATIONAL_UNIFIED_ENTRANCE_EXAMINATION.toString());
    map.put("masterDegreeYear", "2018");
    map.put("masterDegreeRefId", "2");
    map.put("masterDegreeVerification", VerificationStatus.AUTHORIZATION.toString());

  }
}
