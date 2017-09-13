/*
 * Copyright 2017 - 数多科技
 * 
 * 北京数多信息科技有限公司
 * 本公司保留所有下述内容的权利。
 * 本内容为保密信息，仅限本公司内部使用。
 * 非经本公司书面许可，任何人不得外泄或用于其他目的。
 */
package com.digibig.saaserp.person.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtilsBean;
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
import com.digibig.saaserp.commons.exception.DigibigException;
import com.digibig.saaserp.person.domain.Address;
import com.digibig.saaserp.person.service.AddressService;
import com.digibig.saaserp.person.utils.AddressType;
import com.digibig.saaserp.person.utils.Enabled;


/**
 * <p>
 * 地址相关API，本API提供以下接口：<br>
 * 1、添加地址<br>
 * 2、设置地址有效性 <br>
 * 3、查询自然人地址列表<br>
 * </p>
 * 
 * @author libin<libin@we.com>
 * @datetime 2017年9月9日下午16:43
 * @version 1.0
 * @since 1.8
 */
@RestController
@RequestMapping("/v1.0/person/address")
public class AddressController {
  
  private Logger logger = LoggerFactory.getLogger(getClass());
  
  @Autowired
  private AddressService addressService;
  
  /**
   * <p>
   * 添加自然人地址
   * </p>
   * @param paramMap 参数列表
   * <ul>
   *    <li>personId 自然人id</li>
   *    <li>lastNode 最后节点</li>
   *    <li>detailAddress 详细地址</li>
   *    <li>addressType 地址类型（可选）</li>
   *    <li>addressAlias 地址别名（可选）</li>
   *    <li>longitude 经度（可选）</li>
   *    <li>latitude 维度（可选）</li>
   *    <li>postCode 邮编（可选）</li>
   *    <li>isDefault 是否默认（可选）</li>
   * </ul>
   * @return 地址id
   */
  @PostMapping("")
  public HttpResult<Integer> addAddress(@RequestBody Map<String, String> paramMap){
    
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get("personId")), "personId不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get("lastNode")), "lastNode不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get("detailAddress")), "detailAddress不能为空");
    
    Boolean isDeafault = Boolean.valueOf(paramMap.get("isDefault"));

    Address address = new Address();
    BeanUtilsBean beanUtils = BeanUtilsBean.getInstance();
    try {
      beanUtils.populate(address,paramMap);
    } catch (IllegalAccessException e) {
      logger.error(e.getMessage());
      return new HttpResult<Integer>(HttpStatus.PARAM_ERROR,"失败");
    } catch (InvocationTargetException e) {
      logger.error(e.getMessage());
      return new HttpResult<Integer>(HttpStatus.PARAM_ERROR,"失败");
    }
    
    if(!StringUtils.isEmpty(paramMap.get("addressType"))) {
      Integer addressType = Enum.valueOf(AddressType.class, paramMap.get("addressType").trim()).getValue();
      address.setAddressType(addressType);
    }

    logger.info(address.toString());

    Integer id = addressService.addAddress(address, isDeafault);

    if(id == null) {
      return new HttpResult<Integer>(HttpStatus.SERVER_ERROR,"失败,该地址已存在");
    }
    return new HttpResult<Integer>(HttpStatus.OK,"成功",id);
  }
  
  /**
   * <p>
   * 设置地址有效性
   * </p>
   * @param paramMap 参数列表
   * <ul>
   *     <li>personId  自然人id</li>
   *     <li>addressId 地址id</li>
   *     <li>enabled   有效性</li>
   * </ul>
   * @return 操作结果
   */
  @PostMapping("/enabled")
  public HttpResult<Boolean> setEnabled(@RequestBody Map<String, String> paramMap){
    
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get("personId")), "personId不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get("addressId")), "addressId不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get("enabled")), "enabled不能为空");
    
    Integer personId = Integer.valueOf(paramMap.get("personId"));
    Integer addressId = Integer.valueOf(paramMap.get("addressId"));
    Enabled enabled = Enum.valueOf(Enabled.class, paramMap.get("enabled").trim());

    Boolean result = null;
   
    result = addressService.setEnabled(personId, addressId,enabled);
     
    //逻辑判断
    if(result) {
      return new HttpResult<Boolean>(HttpStatus.OK,"成功",result);
    }else {
      return new HttpResult<Boolean>(HttpStatus.SERVER_ERROR,"失败",result);
    }
  }
  
  /**
   * <p>
   * 查询自然人地址信息
   * </p>
   * @param paramMap 参数列表
   * <ul>
   *     <li>personId 自然人id</li>
   *     <li>enabled 有效性（可选）</li>
   * </ul>
   * @return 地址列表
   */
  @PostMapping("/list")
  public HttpResult<List<Map<String ,String>>> getAddress(@RequestBody Map<String, String> paramMap){
    
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get("personId")), "personId不能为空");
    
    Integer personId = Integer.valueOf(paramMap.get("personId"));
    
    Enabled enabled = Enabled.ENABLED;
    
    if(!StringUtils.isEmpty(paramMap.get("enabled"))) {
      enabled = Enum.valueOf(Enabled.class, paramMap.get("enabled").trim());
    }
    
    List<Map<String, String>> addresses = null;
    try {
      addresses = addressService.getAddresses(personId, enabled);
    } catch (DigibigException e) {
      logger.error(e.getMessage());
      return new HttpResult<List<Map<String ,String>>>(HttpStatus.SERVER_ERROR,"地址信息查询失败");
    }
    
    return new HttpResult<List<Map<String ,String>>>(HttpStatus.OK,"成功",addresses);
  }
}
