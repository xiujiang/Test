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
import com.digibig.saaserp.person.common.CommonParam;
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
  
  //最后地址节点
  private static final String LAST_NODE = "lastNode";
  //详细地址
  private static final String DETAIL_ADDRESS = "detailAddress";
  //地址类型
  private static final String ADDRESS_TYPE = "addressType";
  
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
    
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get(CommonParam.MAP_PARAM_PERSONID)), "添加自然人地址personId不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get(LAST_NODE)), "lastNode不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get(DETAIL_ADDRESS)), "添加自然人地址detailAddress不能为空");
    
    Boolean isDeafault = Boolean.valueOf(paramMap.get(CommonParam.MAP_PARAM_ISDEFAULT));

    Address address = new Address();
    BeanUtilsBean beanUtils = BeanUtilsBean.getInstance();
    try {
      beanUtils.populate(address,paramMap);
    } catch (IllegalAccessException | InvocationTargetException e) {
      logger.error("地址对象转换异常",e);
      return new HttpResult<>(HttpStatus.PARAM_ERROR,"失败");
    }
    
    if(!StringUtils.isEmpty(paramMap.get(ADDRESS_TYPE))) {
      Integer addressType = Enum.valueOf(AddressType.class, paramMap.get(ADDRESS_TYPE).trim()).getValue();
      address.setAddressType(addressType);
    }

    logger.info("地址",address);

    Integer id = addressService.addAddress(address, isDeafault);

    if(id == null) {
      return new HttpResult<>(HttpStatus.SERVER_ERROR,"失败,该地址已存在");
    }
    return new HttpResult<>(HttpStatus.OK,"成功",id);
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
    
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get(CommonParam.MAP_PARAM_PERSONID)), "设置地址有效性personId不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get(CommonParam.MAP_PARAM_ADDRESSID)), "设置地址有效性addressId不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get(CommonParam.MAP_PARAM_ENABLED)), "设置地址有效性enabled不能为空");
    
    Integer personId = Integer.valueOf(paramMap.get(CommonParam.MAP_PARAM_PERSONID));
    Integer addressId = Integer.valueOf(paramMap.get(CommonParam.MAP_PARAM_ADDRESSID));
    Enabled enabled = Enum.valueOf(Enabled.class, paramMap.get(CommonParam.MAP_PARAM_ENABLED).trim());

    Boolean result = null;
   
    result = addressService.setEnabled(personId, addressId,enabled);
     
    //逻辑判断
    if(result) {
      return new HttpResult<>(HttpStatus.OK,"成功",result);
    }else {
      return new HttpResult<>(HttpStatus.SERVER_ERROR,"失败",result);
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
    
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get(CommonParam.MAP_PARAM_PERSONID)), "查询自然人地址信息personId不能为空");
    
    Integer personId = Integer.valueOf(paramMap.get(CommonParam.MAP_PARAM_PERSONID));
    
    Enabled enabled = Enabled.ENABLED;
    
    if(!StringUtils.isEmpty(paramMap.get(CommonParam.MAP_PARAM_ENABLED))) {
      enabled = Enum.valueOf(Enabled.class, paramMap.get(CommonParam.MAP_PARAM_ENABLED).trim());
    }
    
    List<Map<String, String>> addresses = null;
    try {
      addresses = addressService.getAddresses(personId, enabled);
    } catch (DigibigException e) {
      logger.error("",e);
      return new HttpResult<>(HttpStatus.SERVER_ERROR,"地址信息查询失败");
    }
    
    return new HttpResult<>(HttpStatus.OK,"成功",addresses);
  }
}
