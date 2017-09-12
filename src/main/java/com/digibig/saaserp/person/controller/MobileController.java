/*
 * Copyright 2017 - 数多科技
 * 
 * 北京数多信息科技有限公司
 * 本公司保留所有下述内容的权利。
 * 本内容为保密信息，仅限本公司内部使用。
 * 非经本公司书面许可，任何人不得外泄或用于其他目的。
 */
package com.digibig.saaserp.person.controller;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.digibig.saaserp.commons.api.HttpResult;
import com.digibig.saaserp.commons.constant.HttpStatus;
import com.digibig.saaserp.commons.util.RegexValidator;
import com.digibig.saaserp.person.service.MobileService;
import com.digibig.saaserp.person.utils.Enabled;

@RestController
@RequestMapping("/v1.0/person/mobiles")
public class MobileController {
  private Logger logger = LoggerFactory.getLogger(getClass());
  
  @Autowired
  private MobileService mobileService;
  
  /**
   * 添加手机号
   * @return
   */
  @PostMapping("")
  public HttpResult<Integer> addMobile(@RequestBody Map<String, String> paramMap){
    
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get("personId")), "personId不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get("isDefault")), "isDefault不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get("mobileNumber")), "mobileNumber不能为空");
    Assert.isTrue(RegexValidator.isMobile(paramMap.get("mobileNumber").trim()), "mobileNumber格式错误");
    
    Integer personId = Integer.valueOf(paramMap.get("personId"));
    String mobileNumber = paramMap.get("mobileNumber");
    Boolean isDefault = Boolean.valueOf(paramMap.get("isDefault"));

    Integer result = mobileService.addMobile(personId, mobileNumber, isDefault);

    if(result == null) {
      return new HttpResult<Integer>(HttpStatus.SERVER_ERROR,"失败",result);
    }
    return new HttpResult<Integer>(HttpStatus.OK,"成功",result);
  }
  

  /**
   * 设置手机号状态
   * @return
   */
  @PostMapping("/enabled")
  public HttpResult<Boolean> setMobileEnabled(@RequestBody Map<String, String> paramMap){
    
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get("personId")), "personId不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get("mobile")), "mobile不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get("enabled")), "enabled不能为空");
    
    Integer personId = Integer.valueOf(paramMap.get("personId"));
    String mobile = paramMap.get("mobile");
    Enabled enabled = Enum.valueOf(Enabled.class, paramMap.get("enabled").trim());
    
    Boolean result = mobileService.setMobileEnabled(personId, mobile, enabled);

    if(result) {
      return new HttpResult<Boolean>(HttpStatus.OK,"成功",result);
    }
    return new HttpResult<Boolean>(HttpStatus.SERVER_ERROR,"失败",result);
  }
  
  
  /**
   * 查询自然人手机号信息 - 脱敏
   * @return
   */
  @PostMapping("/list/des")
  public HttpResult<List<String>> getDesensitizeInfo(@RequestBody Map<String, String> paramMap){
    String personIdStr = paramMap.get("personId");
    String isEnabled = paramMap.get("enabled");
    
    Assert.isTrue(!StringUtils.isEmpty(personIdStr), "personId不能为空");
    
    Integer personId = Integer.valueOf(personIdStr);
    Enabled enabled = Enabled.ENABLED;

    if(!StringUtils.isEmpty(isEnabled)) {
      
      try {
        enabled = Enum.valueOf(Enabled.class, isEnabled.trim());
      }catch(IllegalArgumentException e){
        logger.error(e.getMessage());
        return new HttpResult<List<String>>(HttpStatus.PARAM_ERROR,"enabled必须为Enabled枚举类");
      }
    }
    
    List<String> result = mobileService.getDesensitizeInfo(personId, enabled);
    
    return new HttpResult<List<String>>(HttpStatus.OK,"成功",result);
  }
  
  
  /**
   * 查询自然人手机号信息 - 不脱敏
   * @return
   */
  @PostMapping("/list")
  public HttpResult<List<String>> getMobileInfo(@RequestBody Map<String, String> paramMap){
    String personIdStr = paramMap.get("personId");
    String isEnabled = paramMap.get("enabled");
    String auth = paramMap.get("auth");
    
    Assert.isTrue(!StringUtils.isEmpty(personIdStr), "personId不能为空");
    Assert.isTrue(!StringUtils.isEmpty(auth), "auth不能为空");
    
    Enabled enabled = Enabled.ENABLED;
    
    if(!StringUtils.isEmpty(isEnabled)) {
      
      try {
        enabled = Enum.valueOf(Enabled.class, isEnabled.trim());
      }catch(IllegalArgumentException e){
        logger.error(e.getMessage());
        return new HttpResult<List<String>>(HttpStatus.PARAM_ERROR,"enabled必须为Enabled枚举类");
      }
    }
    //TODO 授权处理
    if(auth == null) {
      return new HttpResult<List<String>>(HttpStatus.AUTH_FAIL,"授权失败");
    }

    Integer personId = Integer.valueOf(personIdStr);
    List<String> result = mobileService.getMobileInfo(personId, enabled);
    return new HttpResult<List<String>>(HttpStatus.OK,"成功",result);
  }
}
