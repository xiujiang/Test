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
import com.digibig.saaserp.person.service.PersonService;

@RestController
@RequestMapping("/v1.0/person")
public class PersonController {
  private Logger logger = LoggerFactory.getLogger(getClass());
  
  @Autowired
  private PersonService personService;
  /**
   * 身份核实
   * @param paramMap
   * @return
   */
  @PostMapping("/veri")
  public HttpResult<Integer> identityVerificate(@RequestBody Map<String, String> paramMap){
    String IDCard = paramMap.get("IDCard");
    String name = paramMap.get("name");
    
    Assert.isTrue(!StringUtils.isEmpty(IDCard), "IDCard不能为空");
    Assert.isTrue(IDValidator.valid(IDCard), "IDCard不合法");
    Assert.isTrue(!StringUtils.isEmpty(name), "name不能为空");
    
    Integer id = personService.identityVerificate(IDCard,name);

    if(id == null) {
      return new HttpResult<Integer>(HttpStatus.PARAM_ERROR,"身份核实失败");
    }
    return new HttpResult<Integer>(HttpStatus.OK,"成功",id);
  }

  /**
   * 按身份证号查询
   * @return
   */
  @PostMapping("/cardno")
  public HttpResult<Map<String,String>> getByCardNumber(@RequestBody Map<String, String> paramMap){
    String IDCard = paramMap.get("IDCard");
    
    Assert.isTrue(!StringUtils.isEmpty(IDCard), "IDCard不能为空");
    Assert.isTrue(IDValidator.valid(IDCard), "IDCard不合法");
    
    Map<String, String> person = null;
    try {
      person = personService.getByCardNumber(IDCard);
    } catch (DigibigException e) {
      logger.error(e.getMessage());
      return new HttpResult<Map<String,String>>(HttpStatus.PARAM_ERROR,e.getMessage());
    }
    
    return new HttpResult<Map<String,String>>(HttpStatus.OK,"成功",person);
  }
  
  /**
   * 设置首选手机号
   * @return
   */
  @PostMapping("/mobile/default")
  public HttpResult<Boolean> setMobile(@RequestBody Map<String, String> paramMap){
    String personIdStr = paramMap.get("personId");
    String mobile = paramMap.get("mobile");
    
    Assert.isTrue(!StringUtils.isEmpty(personIdStr), "personId不能为空");
    Assert.isTrue(!StringUtils.isEmpty(mobile), "mobile不能为空");

    Integer personId = Integer.valueOf(personIdStr);
    
    Boolean result = personService.setDefaultMobile(personId,mobile);

    if(result) {
      return new HttpResult<Boolean>(HttpStatus.OK,"成功",result);
    }else {
      return new HttpResult<Boolean>(HttpStatus.SERVER_ERROR,"失败");
    }
  }
  
  /**
   * 清空首选手机号
   * @return
   */
  @PostMapping("/mobile/default/rem")
  public HttpResult<Boolean> delMobile(@RequestBody Map<String, String> paramMap){
    String personIdStr = paramMap.get("personId");
    
    Assert.isTrue(!StringUtils.isEmpty(personIdStr), "personId不能为空");

    Integer personId = Integer.valueOf(personIdStr);
    
    Boolean result = personService.delDefaultMobile(personId);

    
    if(result) {
      return new HttpResult<Boolean>(HttpStatus.OK,"成功",result);
    }else {
      return new HttpResult<Boolean>(HttpStatus.SERVER_ERROR,"失败",result);
    }
      
  }
  
  /**
   * 设置默认邮箱
   * @return
   */
  @PostMapping("/email/default")
  public HttpResult<Boolean> setEmail(@RequestBody Map<String, String> paramMap){
    String personIdStr = paramMap.get("personId");
    String emailIdStr = paramMap.get("emailId");
    
    Assert.isTrue(!StringUtils.isEmpty(personIdStr), "personId不能为空");
    Assert.isTrue(!StringUtils.isEmpty(emailIdStr), "emailId不能为空");
    
    Integer personId = Integer.valueOf(personIdStr);
    Integer emailId = Integer.valueOf(emailIdStr);
    
    Boolean result = personService.setDefaultEmail(personId,emailId);

    
    if(result) {
      return new HttpResult<Boolean>(HttpStatus.OK,"成功",result);
    }
    return new HttpResult<Boolean>(HttpStatus.SERVER_ERROR,"失败",result);
  }
  
  /**
   * 清空默认邮箱
   * @return
   */
  @PostMapping("/email/default/rem")
  public HttpResult<Boolean> delEmail(@RequestBody Map<String, String> paramMap){
    String personIdStr = paramMap.get("personId");
    
    Assert.isTrue(!StringUtils.isEmpty(personIdStr), "personId不能为空");
    
    Integer personId = Integer.valueOf(personIdStr);
    
    Boolean result = personService.delDefaultEmail(personId);

    if(result) {
      return new HttpResult<Boolean>(HttpStatus.OK,"成功",result);
    }else {
      return new HttpResult<Boolean>(HttpStatus.SERVER_ERROR,"失败");
    }
    
  }
  
  /**
   * 设置自然人首选地址
   * @return
   */
  @PostMapping("/address/default")
  public HttpResult<Boolean> setDefaultAddress(@RequestBody Map<String, String> paramMap){
    String personIdStr = paramMap.get("personId");
    String addressIdStr = paramMap.get("addressId");
    
    Assert.isTrue(!StringUtils.isEmpty(personIdStr), "personId不能为空");
    Assert.isTrue(!StringUtils.isEmpty(addressIdStr), "addressId不能为空");

    Integer personId = Integer.valueOf(personIdStr);
    Integer addressId = Integer.valueOf(addressIdStr);
    
    Boolean result = personService.setDefaultAddress(personId,addressId);

    
    if(result) {
      return new HttpResult<Boolean>(HttpStatus.OK,"成功");
    }
    return new HttpResult<Boolean>(HttpStatus.SERVER_ERROR,"失败");
  }
  
  /**
   * 清空自然人首选地址
   * @return
   */
  @PostMapping("/address/default/rem")
  public HttpResult<Boolean> remDefaultAddress(@RequestBody Map<String, String> paramMap){
    String personIdStr = paramMap.get("personId");
    
    Assert.isTrue(!StringUtils.isEmpty(personIdStr), "personId不能为空");

    Integer personId = Integer.valueOf(personIdStr);
    
    Boolean result = personService.delDefaultAddress(personId);

    if(result) {
      return new HttpResult<Boolean>(HttpStatus.OK,"成功",result);
    }
    return new HttpResult<Boolean>(HttpStatus.SERVER_ERROR,"失败");
  }
  
  /**
   * 查询自然人信息 - 脱敏
   * @return
   */
  @GetMapping("/{id}/des")
  public HttpResult<Map<String,String>> getDesensitizeInfo(@PathVariable("id") Integer personId){
    
    Map<String, String> result = null;
    try {
      result = personService.getDesensitizeInfo(personId);
    } catch (DigibigException e) {
      logger.error(e.getMessage());
    }

    return new HttpResult<Map<String,String>>(HttpStatus.OK,"成功",result);
  }
  /**
   * 查询自然人信息 - 不脱敏
   * @return
   */
  @PostMapping("/list")
  public HttpResult<Map<String,String>> getPersonInfo(@RequestBody Map<String, String> paramMap){
    
    String personIdStr = paramMap.get("personId");
    String auth = paramMap.get("auth");
    
    Assert.isTrue(!StringUtils.isEmpty(personIdStr), "personId不能为空");
    Assert.isTrue(!StringUtils.isEmpty(auth), "auth不能为空");

    //TODO 授权处理
    if(auth == null) {
      return new HttpResult<Map<String,String>>(HttpStatus.AUTH_FAIL,"授权失败");
    }
    
    Integer personId = Integer.valueOf(personIdStr);
    
    Map<String, String> result = null;
    try {
      result = personService.getPersonInfo(personId);
    } catch (DigibigException e) {
      logger.error(e.getMessage());
    }
    
    return new HttpResult<Map<String,String>>(HttpStatus.OK,"成功",result);
  }
}
