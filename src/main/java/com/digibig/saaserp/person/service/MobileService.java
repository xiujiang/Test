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

import com.digibig.saaserp.person.domain.Mobile;
import com.digibig.saaserp.person.utils.Enabled;

public interface MobileService {
  
  /**
   * 添加手机号
   * @param personId 自然人id
   * @param phoneNumber 手机号
   * @param isDefault 是否默认
   * @return 手机id
   */
  Integer addMobile(Integer personId,String phoneNumber,Boolean isDefault);
  
  /**
   * 设置手机号有效性
   * @param personId 自然人id
   * @param number 手机号或id
   * @param enabled 有效性
   * @return 操作结果
   */
  Boolean setMobileEnabled(Integer personId, String number, Enabled enabled);
  
  /**
   * 获取默认手机号
   * @param personId 自然人id
   * @param mobile 手机号码
   * @return 手机号
   */
  Mobile getMobile(Integer personId,String mobile);
  
  /**
   * 获取手机脱敏信息
   * @param personId 自然人id
   * @param enabled 有效性
   * @return 手机信息
   */
  List<String> getDesensitizeInfo(Integer personId, Enabled enabled);
  
  /**
   * 获取手机信息
   * @param personId 自然人id
   * @param enabled 有效性
   * @return 手机信息
   */
  List<String> getMobileInfo(Integer personId, Enabled enabled);

}
