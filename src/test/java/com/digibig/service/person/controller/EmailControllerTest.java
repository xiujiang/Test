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

import com.digibig.service.person.domain.AuthResult;
import com.digibig.service.person.remote.AuthorizationRemote;
import com.digibig.service.person.remote.RegionTemplateRemote;
import com.digibig.service.person.utils.Enabled;
import com.digibig.spring.api.HttpResult;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.digibig.service.person.PersonServiceApplicationTests;

public class EmailControllerTest extends PersonServiceApplicationTests{
  
  
  /**
   * <p>
   * 添加邮箱测试
   * </p>
   * @throws UnsupportedEncodingException
   * @throws Exception
   */
  @Test
  public void addEmail() throws UnsupportedEncodingException, Exception {

    Map<String, String> map = new HashMap<>();
    map.put("personId", "1");
    map.put("email", "adsdtrffasd@126.com");
    map.put("isDefault", "true");

  }
  
  
  /**
   * <p>
   * 设置邮箱有效性测试
   * </p>
   * @throws UnsupportedEncodingException
   * @throws Exception
   */
  @Test
  public void setEmailEnabled() throws UnsupportedEncodingException, Exception {
    
    Map<String, String> map = new HashMap<>();
    map.put("personId", "1");
    map.put("emailId", "1");
    map.put("enabled", Enabled.ENABLED.toString());

  }
  
  
  /**
   * <p>
   * 获取邮箱信息 - 脱敏 测试
   * </p>
   * @throws UnsupportedEncodingException
   * @throws Exception
   */
  @Test
  public void getDesensitizeInfo() throws UnsupportedEncodingException, Exception {
    
    Map<String, String> map = new HashMap<>();
    map.put("personId", "1");

  }
  
  
  /**
   * <p>
   * 获取邮箱信息 - 不脱敏 测试
   * </p>
   * @throws UnsupportedEncodingException
   * @throws Exception
   */
  @Test
  public void getMobileInfo() throws UnsupportedEncodingException, Exception {
    
    Map<String, String> map = new HashMap<>();
    map.put("personId", "1");
    map.put("auth", "2016-07-04");

  }

  @Autowired
  private AuthorizationRemote authorizationRemote;

  @Autowired
  private RegionTemplateRemote templateRemote;

  @Test
  public void testAuth(){
    AuthResult result = authorizationRemote.auth("622827199112050611","李斌","APPCODE 16b805ba3926465cab51ab1ce39dc4fc");
    System.out.println(result);
  }

  @Test
  public void testRegion(){
    HttpResult<String> result = templateRemote.path(1111);
    System.out.println(result);
  }
}
