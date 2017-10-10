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
import com.digibig.saaserp.person.api.remote.CredentialRemote;
import com.digibig.saaserp.person.common.CommonParam;
import com.digibig.saaserp.person.service.MobileService;
import com.digibig.saaserp.person.utils.Enabled;


/**
 * <p>
 * 手机相关API，本API提供以下接口：<br>
 * 1、添加手机号<br>
 * 2、设置手机号有效性 <br>
 * 3、查询自然人手机号信息 - 脱敏<br>
 * 4、查询自然人手机号信息 - 不脱敏<br>
 * </p>
 * 
 * @author libin<libin@we.com>
 * @datetime 2017年9月9日下午16:43
 * @version 1.0
 * @since 1.8
 */
@RestController
@RequestMapping("/v1.0/person/mobiles")
public class MobileController {
  
  private Logger logger = LoggerFactory.getLogger(getClass());
  
  @Autowired
  private CredentialRemote credentialRemote;
  
  @Autowired
  private MobileService mobileService;
  
  /**
   * <p>
   * 添加手机号
   * </p>
   * @param paramMap
   * <ul>
   *    <li>personId 自然人id</li>
   *    <li>number 手机号</li>
   *    <li>isDefault 是否默认（可选）</li>
   * </ul>
   * @return 手机id
   */
  @PostMapping("")
  public HttpResult<Integer> addMobile(@RequestBody Map<String, String> paramMap){
    
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get(CommonParam.MAP_PARAM_PERSONID)), "添加手机号personId不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get(CommonParam.MAP_PARAM_NUMBER)), "number不能为空");
    Assert.isTrue(RegexValidator.isMobile(paramMap.get(CommonParam.MAP_PARAM_NUMBER).trim()), "number格式错误");
    
    Integer personId = Integer.valueOf(paramMap.get(CommonParam.MAP_PARAM_PERSONID));
    String mobileNumber = paramMap.get(CommonParam.MAP_PARAM_NUMBER);
    Boolean isDefault = Boolean.valueOf(CommonParam.MAP_PARAM_ISDEFAULT);
    
    logger.info("personId:{},mobile:{}",personId,mobileNumber);

    Integer result = mobileService.addMobile(personId, mobileNumber, isDefault);

    if(result == null) {
      return new HttpResult<>(HttpStatus.SERVER_ERROR,"失败",result);
    }
    return new HttpResult<>(HttpStatus.OK,"成功",result);
  }
  

  /**
   * <p>
   * 设置手机号状态
   * </p>
   * @param paramMap
   * <ul>
   *    <li>personId 自然人id</li>
   *    <li>mobile 手机信息</li>
   *    <li>enabled 有效性</li>
   * </ul>
   * @return 操作结果
   */
  @PostMapping("/enabled")
  public HttpResult<Boolean> setMobileEnabled(@RequestBody Map<String, String> paramMap){
    
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get(CommonParam.MAP_PARAM_PERSONID)), "设置手机号状态personId不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get(CommonParam.MAP_PARAM_MOBILE)), "设置手机号状态mobile不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get(CommonParam.MAP_PARAM_ENABLED)), "设置手机号状态enabled不能为空");
    
    Integer personId = Integer.valueOf(paramMap.get(CommonParam.MAP_PARAM_PERSONID));
    String mobile = paramMap.get(CommonParam.MAP_PARAM_MOBILE);
    Enabled enabled = Enum.valueOf(Enabled.class, paramMap.get(CommonParam.MAP_PARAM_ENABLED).trim());
    
    logger.info("personId:{},mobile:{}",personId,mobile);
    
    Boolean result = mobileService.setMobileEnabled(personId, mobile, enabled);

    if(result) {
      return new HttpResult<>(HttpStatus.OK,"成功",result);
    }
    return new HttpResult<>(HttpStatus.SERVER_ERROR,"失败",result);
  }
  
  
  /**
   * <p>
   * 查询自然人手机号信息 - 脱敏
   * </p>
   * @param paramMap
   * <ul>
   *    <li>personId 自然人id</li>
   *    <li>enabled 有效性（可选）</li>
   * </ul>
   * @return 手机号信息
   */
  @PostMapping("/list/des")
  public HttpResult<List<String>> getDesensitizeInfo(@RequestBody Map<String, String> paramMap){
    String personIdStr = paramMap.get(CommonParam.MAP_PARAM_PERSONID);
    String isEnabled = paramMap.get(CommonParam.MAP_PARAM_ENABLED);
    
    Assert.isTrue(!StringUtils.isEmpty(personIdStr), "查询自然人手机号信息 - 脱敏personId不能为空");
    
    Integer personId = Integer.valueOf(personIdStr);
    Enabled enabled = Enabled.ENABLED;

    if(!StringUtils.isEmpty(isEnabled)) {
        enabled = Enum.valueOf(Enabled.class, isEnabled.trim());
    }
    
    List<String> result = mobileService.getDesensitizeInfo(personId, enabled);
    
    return new HttpResult<>(HttpStatus.OK,"成功",result);
  }
  
  
  /**
   * <p>
   * 查询自然人手机号信息 - 不脱敏
   * </p>
   * @param paramMap
   * <ul>
   *    <li>personId 自然人id</li>
   *    <li>auth 授权码</li>
   *    <li>enabled 有效性（可选）</li>
   * </ul>
   * @return 手机号信息
   */
  @PostMapping("/list")
  public HttpResult<List<String>> getMobileInfo(@RequestBody Map<String, String> paramMap){
    String personIdStr = paramMap.get(CommonParam.MAP_PARAM_PERSONID);
    String isEnabled = paramMap.get(CommonParam.MAP_PARAM_ENABLED);
    String auth = paramMap.get(CommonParam.MAP_PARAM_AUTH);
    
    Assert.isTrue(!StringUtils.isEmpty(personIdStr), "查询自然人手机号信息personId不能为空");
    Assert.isTrue(!StringUtils.isEmpty(auth), "查询自然人手机号信息auth不能为空");
    
    Enabled enabled = Enabled.ENABLED;
    
    if(!StringUtils.isEmpty(isEnabled)) {
        enabled = Enum.valueOf(Enabled.class, isEnabled.trim());
    }
    
    //验证授权信息
    HttpResult<Void> httpresult = credentialRemote.verify(auth);
    
    if (httpresult.getCode() != HttpStatus.OK) {
      return new HttpResult<>(HttpStatus.AUTH_FAIL,"授权失败");
    }

    Integer personId = Integer.valueOf(personIdStr);
    List<String> result = mobileService.getMobileInfo(personId, enabled);
    return new HttpResult<>(HttpStatus.OK,"成功",result);
  }
}
