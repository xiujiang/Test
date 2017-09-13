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

import com.digibig.saaserp.person.utils.Enabled;

public interface EmailService {
  
  /**
   * 添加邮箱
   * @param personId 自然人id
   * @param email 邮箱
   * @param isDefault 是否默认
   * @return 邮箱id
   */
  Integer addEmail(Integer personId, String email, Boolean isDefault) ;
  
  /**
   * 设置邮箱有效性
   * @param personId 自然人id
   * @param emailId 邮箱id
   * @param enabled 有效性
   * @return 操作结果
   */
  Boolean setEmailEnabled(Integer personId, Integer emailId, Enabled enabled) ;
  
  /**
   * 获取脱敏信息
   * @param personId 自然人id
   * @param enabled 有效性
   * @return 邮箱信息
   */
  List<String> getDesensitizeInfo(Integer personId, Enabled enabled);
  
  /**
   * 获取邮箱信息
   * @param personId 自然人id
   * @param enabled 有效性
   * @return 邮箱信息
   */
  List<String> getEmailInfo(Integer personId, Enabled enabled);

}
