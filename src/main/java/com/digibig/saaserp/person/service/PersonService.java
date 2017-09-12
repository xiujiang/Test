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
  
  Integer getDefaultMobile(Integer personId);
  
  Boolean delDefaultMobile(Integer personId);
  
  Integer getDefaultEmail(Integer personId);
  
  Integer getdefaultAddress(Integer personId);
  
  Integer getdefaultIDCard(Integer personId);
  
  Boolean delDefaultEmail(Integer personId);
  
  Boolean delDefaultAddress(Integer personId);
  
  Integer identityVerificate(String IDCard, String name);
  
  Map<String, String> getByCardNumber(String IDCard) throws DigibigException;
  
  Map<String, String> getDesensitizeInfo(Integer personId) throws DigibigException;
  
  Map<String, String> getPersonInfo(Integer personId) throws DigibigException;
  
  Boolean setDefaultMobile(Integer personId, Integer mobileId);
  
  Boolean setDefaultMobile(Integer personId, String mobile);
  
  Boolean setDefaultIDCard(Integer personId, Integer idCardId);
  
  Boolean setDefaultEmail(Integer personId, Integer mobileId);

  Boolean setDefaultAddress(Integer personId, Integer addressId);
}
