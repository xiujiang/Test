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

import com.digibig.service.person.utils.AddressType;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.springframework.http.MediaType;

import com.digibig.service.person.PersonServiceApplicationTests;
import com.digibig.service.person.utils.Enabled;

public class AddressControllerTest extends PersonServiceApplicationTests {
  /**
   * <p>
   * 添加地址测试
   * </p>
   * @throws UnsupportedEncodingException
   * @throws Exception
   */
  @Test
  public void addAddress() throws UnsupportedEncodingException, Exception {
    
    Map<String, String> map = new HashMap<>();
    map.put("personId", "1");
    map.put("lastNode", "110107");
    map.put("detailAddress", "复兴路65号院");
    map.put("addressType", AddressType.COMPANY.toString());
    map.put("addressAlias", "和仁");
    map.put("longitude", "26.2654");
    map.put("latitude", "66.5425");
    map.put("postCode", "100081");
    map.put("isDefault", "false");

  }
  
  /**
   * <p>
   * 设置地址有效性测试
   * </p>
   * @throws UnsupportedEncodingException
   * @throws Exception
   */
  @Test
  public void setEnabled() throws UnsupportedEncodingException, Exception {
    
    Map<String, String> map = new HashMap<>();
    map.put("personId", "1");
    map.put("addressId", "4");
    map.put("enabled", Enabled.ENABLED.toString());

  }
  
  /**
   * <p>
   * 获取自然人地址信息
   * </p>
   * @throws UnsupportedEncodingException
   * @throws Exception
   */
  @Test
  public void getAddress() throws UnsupportedEncodingException, Exception {
    
    Map<String, String> map = new HashMap<>();
    map.put("personId", "1");
    map.put("enabled", Enabled.ENABLED.toString());

  }
}
