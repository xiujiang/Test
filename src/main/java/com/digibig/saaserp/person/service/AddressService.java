/*
 * Copyright 2017 - 数多科技
 * 
 * 北京数多信息科技有限公司
 * 本公司保留所有下述内容的权利。
 * 本内容为保密信息，仅限本公司内部使用。
 * 非经本公司书面许可，任何人不得外泄或用于其他目的。
 */
package com.digibig.saaserp.person.service;


import java.util.List;
import java.util.Map;

import com.digibig.saaserp.commons.exception.DigibigException;
import com.digibig.saaserp.person.domain.Address;
import com.digibig.saaserp.person.utils.Enabled;

public interface AddressService {

  /**
   * 添加地址
   * @param address 地址对象 
   * @param isDefault 是否默认
   * @return 地址id
   */
  Integer addAddress(Address address, Boolean isDefault);
  
  /**
   * 设置地址有效性
   * @param personId 自然人id
   * @param addressId 地址id
   * @param enabled 有效性
   * @return 操作结果
   */
  Boolean setEnabled(Integer personId,Integer addressId,Enabled enabled) ;
  
  /**
   * 获取自然人地址列表
   * @param personId 自然人id
   * @param enabled 有效性标识
   * @return 地址列表
   * @throws DigibigException 
   */
  List<Map<String ,String>> getAddresses(Integer personId,Enabled enabled) throws DigibigException ;
  
  /**
   * 根据id获取地址
   * @param id 地址id
   * @return 地址信息
   * @throws DigibigException
   */
  String getAddressById(Integer id) throws DigibigException;
}
