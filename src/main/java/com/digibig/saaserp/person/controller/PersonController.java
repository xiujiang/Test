/*
 * Copyright 2017 - 数多科技
 * 
 * 北京数多信息科技有限公司
 * 本公司保留所有下述内容的权利。
 * 本内容为保密信息，仅限本公司内部使用。
 * 非经本公司书面许可，任何人不得外泄或用于其他目的。
 */
package com.digibig.saaserp.person.controller;


import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.digibig.saaserp.commons.api.HttpResult;
import com.digibig.saaserp.commons.constant.HttpStatus;
import com.digibig.saaserp.commons.exception.DigibigException;
import com.digibig.saaserp.commons.util.IDValidator;
import com.digibig.saaserp.person.api.remote.CredentialRemote;
import com.digibig.saaserp.person.common.CommonParam;
import com.digibig.saaserp.person.service.PersonService;


/**
 * <p>
 * 自然人相关API，本API提供以下接口：<br>
 * 1、身份核实<br>
 * 2、按身份证号查询 <br>
 * 3、设置首选手机号<br>
 * 4、清空首选手机号<br>
 * 5、设置默认邮箱<br>
 * 6、清空默认邮箱<br>
 * 7、设置自然人首选地址<br>
 * 8、清空自然人首选地址<br>
 * 9、查询自然人信息 - 脱敏<br>
 * 10、查询自然人信息 - 不脱敏<br>
 * </p>
 * 
 * @author libin<libin@we.com>
 * @datetime 2017年9月9日下午16:43
 * @version 1.0
 * @since 1.8
 */
@RestController
@RequestMapping("/v1.0/person")
public class PersonController {
  private Logger logger = LoggerFactory.getLogger(getClass());
  
  @Autowired
  private CredentialRemote credentialRemote;
  
  @Autowired
  private PersonService personService;
  
  /**
   * <p>
   * 身份核实
   * </p>
   * @param paramMap
   * <ul>
   *    <li>idCard 身份证号</li>
   *    <li>name 姓名</li>
   * </ul>
   * @return 自然人id
   */
  @PostMapping("/veri")
  public HttpResult<Map<String , Object>> identityVerificate(@RequestBody Map<String, String> paramMap){
    String idCard = paramMap.get(CommonParam.MAP_PARAM_IDCARD);
    String name = paramMap.get(CommonParam.MAP_PARAM_NAME);
    
    Assert.isTrue(!StringUtils.isEmpty(idCard), "身份核实idCard不能为空");
    Assert.isTrue(IDValidator.valid(idCard), "身份核实idCard不合法");
    Assert.isTrue(!StringUtils.isEmpty(name), "身份核实name不能为空");
    
    Map<String , Object> map = personService.identityVerificate(idCard,name);

    if(map == null) {
      return new HttpResult<>(HttpStatus.PARAM_ERROR,"身份核实失败");
    }
    return new HttpResult<>(HttpStatus.OK,"成功",map);
  }

  /**
   * <p>
   * 按身份证号查询
   * </p>
   * @param paramMap
   * <ul>
   *    <li>idCard 身份证号</li>
   * </ul>
   * @return 自然人信息
   */
  @PostMapping("/cardno")
  public HttpResult<Map<String,Object>> getByCardNumber(@RequestBody Map<String, String> paramMap){
    String idCard = paramMap.get(CommonParam.MAP_PARAM_IDCARD);
    
    Assert.isTrue(!StringUtils.isEmpty(idCard), "按身份证号查询idCard不能为空");
    Assert.isTrue(IDValidator.valid(idCard), "按身份证号查询idCard不合法");
    
    Map<String, Object> person = null;
    try {
      person = personService.getByCardNumber(idCard);
    } catch (DigibigException e) {
      logger.error("按身份证号查询自然人信息",e);
      return new HttpResult<>(HttpStatus.PARAM_ERROR,e.getMessage());
    }
    
    return new HttpResult<>(HttpStatus.OK,"成功",person);
  }
  
  /**
   * <p>
   * 设置首选手机号
   * </p>
   * @param paramMap
   * <ul>
   *    <li>personId 自然人id</li>
   *    <li>mobile 手机信息</li>
   * </ul>
   * @return 操作结果
   */
  @PostMapping("/mobile/default")
  public HttpResult<Boolean> setMobile(@RequestBody Map<String, String> paramMap){
    String personIdStr = paramMap.get(CommonParam.MAP_PARAM_PERSONID);
    String mobile = paramMap.get(CommonParam.MAP_PARAM_MOBILE);
    
    Assert.isTrue(!StringUtils.isEmpty(personIdStr), "设置首选手机号personId不能为空");
    Assert.isTrue(!StringUtils.isEmpty(mobile), "设置首选手机号mobile不能为空");

    Integer personId = Integer.valueOf(personIdStr);
    
    Boolean result = personService.setDefaultMobile(personId,mobile);

    if(result) {
      return new HttpResult<>(HttpStatus.OK,"成功",result);
    }else {
      return new HttpResult<>(HttpStatus.SERVER_ERROR,"失败");
    }
  }
  
  /**
   * <p>
   * 清空首选手机号
   * </p>
   * @param paramMap
   * <ul>
   *    <li>personId 自然人id</li>
   * </ul>
   * @return 操作结果
   */
  @PostMapping("/mobile/default/rem")
  public HttpResult<Boolean> delMobile(@RequestBody Map<String, String> paramMap){
    String personIdStr = paramMap.get(CommonParam.MAP_PARAM_PERSONID);
    
    Assert.isTrue(!StringUtils.isEmpty(personIdStr), "清空首选手机号personId不能为空");

    Integer personId = Integer.valueOf(personIdStr);
    
    Boolean result = personService.delDefaultMobile(personId);

    if(result) {
      return new HttpResult<>(HttpStatus.OK,"成功",result);
    }else {
      return new HttpResult<>(HttpStatus.SERVER_ERROR,"失败",result);
    }
      
  }
  
  /**
   * <p>
   * 设置默认邮箱
   * </p>
   * @param paramMap
   * <ul>
   *    <li>personId 自然人id</li>
   *    <li>emailId 邮箱id</li>
   * </ul>
   * @return 操作结果
   */
  @PostMapping("/email/default")
  public HttpResult<Boolean> setEmail(@RequestBody Map<String, String> paramMap){
    String personIdStr = paramMap.get(CommonParam.MAP_PARAM_PERSONID);
    String emailIdStr = paramMap.get(CommonParam.MAP_PARAM_EMAILID);
    
    Assert.isTrue(!StringUtils.isEmpty(personIdStr), "设置默认邮箱personId不能为空");
    Assert.isTrue(!StringUtils.isEmpty(emailIdStr), "设置默认邮箱emailId不能为空");
    
    Integer personId = Integer.valueOf(personIdStr);
    Integer emailId = Integer.valueOf(emailIdStr);
    
    Boolean result = personService.setDefaultEmail(personId,emailId,false);
    
    if(result) {
      return new HttpResult<>(HttpStatus.OK,"成功",result);
    }
    return new HttpResult<>(HttpStatus.SERVER_ERROR,"失败",result);
  }
  
  /**
   * <p>
   * 清空默认邮箱
   * </p>
   * @param paramMap
   * <ul>
   *    <li>personId 自然人id</li>
   * </ul>
   * @return 操作结果
   */
  @PostMapping("/email/default/rem")
  public HttpResult<Boolean> delEmail(@RequestBody Map<String, String> paramMap){
    String personIdStr = paramMap.get(CommonParam.MAP_PARAM_PERSONID);
    
    Assert.isTrue(!StringUtils.isEmpty(personIdStr), "清空默认邮箱personId不能为空");
    
    Integer personId = Integer.valueOf(personIdStr);
    
    Boolean result = personService.delDefaultEmail(personId);

    if(result) {
      return new HttpResult<>(HttpStatus.OK,"成功",result);
    }else {
      return new HttpResult<>(HttpStatus.SERVER_ERROR,"失败");
    }
  }
  
  /**
   * <p>
   * 设置自然人首选地址
   * </p>
   * @param paramMap
   * <ul>
   *    <li>personId 自然人id</li>
   *    <li>addressId 地址id</li>
   * </ul>
   * @return 操作结果
   */
  @PostMapping("/address/default")
  public HttpResult<Boolean> setDefaultAddress(@RequestBody Map<String, String> paramMap){
    String personIdStr = paramMap.get(CommonParam.MAP_PARAM_PERSONID);
    String addressIdStr = paramMap.get(CommonParam.MAP_PARAM_ADDRESSID);
    
    Assert.isTrue(!StringUtils.isEmpty(personIdStr), "设置自然人首选地址personId不能为空");
    Assert.isTrue(!StringUtils.isEmpty(addressIdStr), "设置自然人首选地址addressId不能为空");

    Integer personId = Integer.valueOf(personIdStr);
    Integer addressId = Integer.valueOf(addressIdStr);
    
    Boolean result = personService.setDefaultAddress(personId,addressId,false);
    
    if(result) {
      return new HttpResult<>(HttpStatus.OK,"成功",result);
    }
    return new HttpResult<>(HttpStatus.SERVER_ERROR,"失败",result);
  }
  
  /**
   * <p>
   * 清空自然人首选地址
   * </p>
   * @param paramMap
   * <ul>
   *    <li>personId 自然人id</li>
   * </ul>
   * @return 操作结果
   */
  @PostMapping("/address/default/rem")
  public HttpResult<Boolean> remDefaultAddress(@RequestBody Map<String, String> paramMap){
    String personIdStr = paramMap.get(CommonParam.MAP_PARAM_PERSONID);
    
    Assert.isTrue(!StringUtils.isEmpty(personIdStr), "清空自然人首选地址personId不能为空");

    Integer personId = Integer.valueOf(personIdStr);
    
    Boolean result = personService.delDefaultAddress(personId);

    if(result) {
      return new HttpResult<>(HttpStatus.OK,"成功",result);
    }
    return new HttpResult<>(HttpStatus.SERVER_ERROR,"失败");
  }
  
  /**
   * <p>
   * 查询自然人信息 - 脱敏
   * </p>
   * @param id 自然人id
   * @return 自然人信息
   */
  @GetMapping("/{id}/des")
  public HttpResult<Map<String,Object>> getDesensitizeInfo(@PathVariable("id") Integer personId){
    
    Map<String, Object> result = null;
    try {
      result = personService.getDesensitizeInfo(personId);
    } catch (DigibigException e) {
      logger.error("查询自然人信息 - 脱敏:",e);
    }

    return new HttpResult<>(HttpStatus.OK,"成功",result);
  }
  
  /**
   * <p>
   * 查询自然人信息 - 不脱敏
   * </p>
   * @param paramMap
   * <ul>
   *    <li>personId 自然人id</li>
   *    <li>auth 授权码</li>
   * </ul>
   * @return 自然人信息
   */
  @PostMapping("/list")
  public HttpResult<Map<String,Object>> getPersonInfo(@RequestBody Map<String, String> paramMap){
    
    String personIdStr = paramMap.get(CommonParam.MAP_PARAM_PERSONID);
    String auth = paramMap.get(CommonParam.MAP_PARAM_AUTH);
    
    Assert.isTrue(!StringUtils.isEmpty(personIdStr), "查询自然人信息personId不能为空");
    Assert.isTrue(!StringUtils.isEmpty(auth), "查询自然人信息auth不能为空");
    
    //验证授权信息
    HttpResult<Void> httpresult = credentialRemote.verify(auth);
    
    if (httpresult.getCode() != HttpStatus.OK) {
      return new HttpResult<>(HttpStatus.AUTH_FAIL,"授权失败");
    }
    
    Integer personId = Integer.valueOf(personIdStr);
    
    Map<String, Object> result = null;
    try {
      result = personService.getPersonInfo(personId);
    } catch (DigibigException e) {
      logger.error("查询自然人信息 - 不脱敏:",e);
      return new HttpResult<>(HttpStatus.SERVER_ERROR,"失败");
    }
    
    return new HttpResult<>(HttpStatus.OK,"成功",result);
  }
}
