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
import com.digibig.saaserp.person.service.EmailService;
import com.digibig.saaserp.person.utils.Enabled;


/**
 * <p>
 * 邮箱相关API，本API提供以下接口：<br>
 * 1、添加邮箱<br>
 * 2、设置邮箱有效性 <br>
 * 3、查询自然人邮箱信息 - 脱敏<br>
 * 4、查询自然人邮箱信息 - 不脱敏<br>
 * </p>
 * 
 * @author libin<libin@we.com>
 * @datetime 2017年9月9日下午16:43
 * @version 1.0
 * @since 1.8
 */
@RestController
@RequestMapping("/v1.0/person/email")
public class EmailController {
  private Logger logger = LoggerFactory.getLogger(getClass());
  
  @Autowired
  private EmailService emailService;
  
  /**
   * <p>
   * 添加邮箱
   * </p>
   * @param paramMap
   *<ul>
   *    <li>personId 自然人id</li>
   *    <li>email 邮箱</li>
   *    <li>isDefault 是否默认（可选）</li>
   * </ul>
   * @return 邮箱id
   */
  @PostMapping("")
  public HttpResult<Integer> addEmail(@RequestBody Map<String, String> paramMap){
    String email = paramMap.get("email");
    
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get("personId")), "personId不能为空");
    Assert.isTrue(!StringUtils.isEmpty(email.trim()), "email不能为空");
    Assert.isTrue(RegexValidator.isEmail(email.trim()), "email格式错误");
    
    Integer personId = Integer.valueOf(paramMap.get("personId"));
    Boolean isDefault = Boolean.valueOf(paramMap.get("isDefault"));
    
    Integer result = null;
    result = emailService.addEmail(personId, email, isDefault);

    if(result == null) {
      return new HttpResult<Integer>(HttpStatus.SERVER_ERROR,"失败");
    }
    return new HttpResult<Integer>(HttpStatus.OK,"成功",result);
  }

  /**
   * <p>
   * 设置邮箱状态
   * </p>
   * @param paramMap
   * <ul>
   *    <li>personId 自然人id</li>
   *    <li>emailId 邮箱id</li>
   *    <li>enabled 有效性</li>
   * </ul>
   * @return 操作结果
   */
  @PostMapping("/enabled")
  public HttpResult<Boolean> setEmailEnabled(@RequestBody Map<String, String> paramMap){
    
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get("personId")), "personId不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get("emailId")), "emailId不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get("enabled")), "enabled不能为空");
    
    Integer personId = Integer.valueOf(paramMap.get("personId"));
    Integer emailId = Integer.valueOf(paramMap.get("emailId"));
    Enabled enabled = Enum.valueOf(Enabled.class, paramMap.get("enabled").trim());
    
    Boolean result = emailService.setEmailEnabled(personId, emailId, enabled);

    if(result) {
      return new HttpResult<Boolean>(HttpStatus.OK,"成功",result);
    }
    
    return new HttpResult<Boolean>(HttpStatus.SERVER_ERROR,"失败",result);
    
  }
  /**
   * <p>
   * 查询自然人邮箱信息 - 脱敏
   * </p>
   * @param paramMap
   * <ul>
   *    <li>personId 自然人id</li>
   *    <li>enabled 有效性（可选）</li>
   * </ul>
   * @return 邮箱信息
   */
  @PostMapping("/list/des")
  public HttpResult<List<String>> getDesensitizeInfo(@RequestBody Map<String, String> paramMap){
    
    String personIdStr = paramMap.get("personId");
    String isEnabled = paramMap.get("enabled");
    
    Assert.isTrue(!StringUtils.isEmpty(personIdStr), "personId不能为空");
    
    Enabled enabled = Enabled.ENABLED;

    if(!StringUtils.isEmpty(isEnabled)) {
      try {
        enabled = Enum.valueOf(Enabled.class, isEnabled.trim());
      }catch(IllegalArgumentException ex){
        logger.error(ex.getMessage());
        return new HttpResult<List<String>>(HttpStatus.PARAM_ERROR,"enabled必须为Enabled枚举类");
      }
    }
    
    Integer personId = Integer.valueOf(personIdStr);
    
    List<String> result = emailService.getDesensitizeInfo(personId, enabled);
    
    return new HttpResult<List<String>>(HttpStatus.OK,"成功",result);
  }
  
  /**
   * <p>
   * 查询自然人邮箱信息 - 不脱敏
   * </p>
   * @param paramMap
   * <ul>
   *    <li>personId 自然人id</li>
   *    <li>enabled 有效性（可选）</li>
   *    <li>auth 授权码</li>
   * </ul>
   * @return 邮箱信息
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
      }catch(IllegalArgumentException ex){
        logger.error(ex.getMessage());
        return new HttpResult<List<String>>(HttpStatus.PARAM_ERROR,"enabled必须为Enabled枚举类");
      }
    }
    
    //TODO 授权处理
    if(auth == null) {
      return new HttpResult<List<String>>(HttpStatus.AUTH_FAIL,"授权失败");
    }

    Integer personId = Integer.valueOf(personIdStr);
    
    List<String> result = emailService.getEmailInfo(personId, enabled);
    
    return new HttpResult<List<String>>(HttpStatus.OK,"成功",result);
  }
}
