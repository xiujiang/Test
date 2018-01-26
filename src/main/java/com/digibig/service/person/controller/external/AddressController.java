/*
 * Copyright 2017 - 数多科技
 * 
 * 北京数多信息科技有限公司
 * 本公司保留所有下述内容的权利。
 * 本内容为保密信息，仅限本公司内部使用。
 * 非经本公司书面许可，任何人不得外泄或用于其他目的。
 */
package com.digibig.service.person.controller.external;

import com.digibig.service.person.domain.Address;
import com.digibig.service.person.service.AddressService;
import com.digibig.service.person.utils.AddressType;
import com.digibig.service.person.utils.Enabled;
import com.digibig.spring.api.HttpResult;
import com.digibig.spring.api.HttpStatus;
import com.digibig.spring.exception.DigibigException;
import java.lang.reflect.InvocationTargetException;
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

import com.digibig.service.person.common.CommonParam;


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
@RestController("AddressController-e")
@RequestMapping("/v1.0/person/address")
public class AddressController {
  
  private Logger logger = LoggerFactory.getLogger(getClass());

  private com.digibig.service.person.controller.internal.AddressController controller;
  
  @Autowired
  private AddressService addressService;
  
  //最后地址节点
  private static final String LAST_NODE = "lastNode";
  //详细地址
  private static final String DETAIL_ADDRESS = "detailAddress";
  //地址类型
  private static final String ADDRESS_TYPE = "addressType";
  

  @PostMapping("add")
  public HttpResult<Address> addAddress(@RequestBody Address address){

    return new HttpResult<>(HttpStatus.OK,"成功",addressService.add(address));
  }

  /**
   * 添加限制-只修改状态
   * @param address
   * @return
   */
  @PostMapping("/update")
  public HttpResult<Address> setEnabled(@RequestBody Address address){

    return controller.updateSelectiveJson(address);

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
  public HttpResult<List<Address>> getAddress(@RequestBody Map<String, String> paramMap){
    
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get(CommonParam.MAP_PARAM_PERSONID)), "查询自然人地址信息personId不能为空");
    
    Integer personId = Integer.valueOf(paramMap.get(CommonParam.MAP_PARAM_PERSONID));
    
    Enabled enabled = Enabled.ENABLED;
    
    if(!StringUtils.isEmpty(paramMap.get(CommonParam.MAP_PARAM_ENABLED))) {
      enabled = Enum.valueOf(Enabled.class, paramMap.get(CommonParam.MAP_PARAM_ENABLED).trim());
    }
    
    List<Address> addresses = null;
    try {
      addresses = addressService.listWithParent(personId);
    } catch (DigibigException e) {
      logger.error("",e);
      return new HttpResult<>(HttpStatus.SERVER_ERROR,"地址信息查询失败");
    }
    
    return new HttpResult<>(HttpStatus.OK,"成功",addresses);
  }
}
