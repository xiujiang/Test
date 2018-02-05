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
import com.digibig.spring.api.HttpResult;
import com.digibig.spring.api.HttpStatus;
import java.util.Collection;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


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

  @Autowired
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

  @PostMapping("/list")
  public HttpResult<Collection<Address>> getAddress(
      @RequestParam(required=false,name="id") Integer id,
      @RequestParam(required=false,name="idList") List<Integer> idList,
      @RequestParam(required=false,name="customerId") Integer customerId,
      @RequestParam(required=false,name="customerIdList") List<Integer> customerIdList){
    
    return controller.list(id,idList,null,null,customerId,customerIdList);
  }
}
