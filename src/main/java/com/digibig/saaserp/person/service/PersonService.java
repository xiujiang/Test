/*
 * Copyright 2017 - 数多科技
 * 
 * 北京数多信息科技有限公司
 * 本公司保留所有下述内容的权利。
 * 本内容为保密信息，仅限本公司内部使用。
 * 非经本公司书面许可，任何人不得外泄或用于其他目的。
 */
package com.digibig.saaserp.person.service;

import java.util.Map;

import com.digibig.saaserp.commons.exception.DigibigException;

public interface PersonService {
  /**
   * 获取默认手机id
   * @param personId 自然人id
   * @return 手机id
   */
  Integer getDefaultMobile(Integer personId);
  
  /**
   * 清空默认手机号
   * @param personId 自然人id
   * @return 操作结果
   */
  Boolean delDefaultMobile(Integer personId);
  
  /**
   * 手机号id为mobileId时清空默认手机号
   * @param personId 自然人id
   * @param mobileId 手机号id
   * @return 操作结果
   */
  Boolean delDefaultMobile(Integer personId, Integer mobileId);
  
  /**
   * 获取默认邮箱
   * @param personId 自然人id
   * @return 邮箱id
   */
  Integer getDefaultEmail(Integer personId);
  
  /**
   * 获取默认地址
   * @param personId 自然人id
   * @return 地址id
   */
  Integer getdefaultAddress(Integer personId);
  
  /**
   * 获取默认身份证id
   * @param personId 自然人id
   * @return 身份证id
   */
  Integer getdefaultIDCard(Integer personId);
  
  /**
   * 清除默认邮箱
   * @param personId 自然人id
   * @return 操作结果
   */
  Boolean delDefaultEmail(Integer personId);
  
  /**
   * 清除默认地址
   * @param personId 自然人id
   * @return 操作结果
   */
  Boolean delDefaultAddress(Integer personId);
  
  /**
   * 默认邮箱id为emailId时清除默认邮箱
   * @param personId 自然人id
   * @param emailId 邮箱id
   * @return 操作结果
   */
  Boolean delDefaultEmail(Integer personId, Integer emailId);
  
  /**
   * 默认地址id为addressid时清除默认地址
   * @param personId 自然人id
   * @param addressId 地址id
   * @return 操作结果
   */
  Boolean delDefaultAddress(Integer personId, Integer addressId);
  
  /**
   * 身份认证
   * @param IDCard 身份证号
   * @param name 姓名
   * @return 自然人id
   * @throws DigibigException 
   */
  Map<String , String> identityVerificate(String idCard, String name) throws DigibigException;
  
  /**
   * 获取自然人信息
   * @param IDCard 身份证号
   * @return 自然人脱敏信息
   * @throws DigibigException
   */
  Map<String, Object> getByCardNumber(String idCard) throws DigibigException;
  
  /**
   * 获取自然人脱敏信息
   * @param personId 自然人id
   * @return 自然人脱敏信息
   * @throws DigibigException
   */
  Map<String, Object> getDesensitizeInfo(Integer personId) throws DigibigException;
  
  /**
   * 获取自然人信息
   * @param personId 自然人id
   * @return 自然人信息
   * @throws DigibigException
   */
  Map<String, Object> getPersonInfo(Integer personId) throws DigibigException;
  
  /**
   * 设置默认手机
   * @param personId 自然人id
   * @param mobileId 手机id
   * @return 操作结果
   */
  Boolean setDefaultMobile(Integer personId, Integer mobileId);
  
  /**
   * 设置默认手机
   * @param personId 自然人id
   * @param mobile 手机号或id
   * @return 操作结果
   */
  Boolean setDefaultMobile(Integer personId, String mobile);
  
  /**
   * 设置默认身份证
   * @param personId 自然人id
   * @param idCardId 身份证id
   * @return 操作结果
   */
  Boolean setDefaultIDCard(Integer personId, Integer idCardId);
  
  /**
   * 设置默认邮箱
   * @param personId 自然人id
   * @param mobileId 邮箱id
   * @return 操作结果
   */
  Boolean setDefaultEmail(Integer personId, Integer mobileId, Boolean realEmail);

  /**
   * 设置默认地址
   * @param personId 自然人id
   * @param addressId 地址id
   * @param realAddress 地址是否确认有效
   * @return 操作结果
   */
  Boolean setDefaultAddress(Integer personId, Integer addressId, Boolean realAddress);
}
