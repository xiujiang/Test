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
  
  Integer addEmail(Integer personId, String email, Boolean isDefault) ;
  
  Boolean setEmailEnabled(Integer personId, Integer emailId, Enabled enabled) ;
  
  List<String> getDesensitizeInfo(Integer personId, Enabled enabled);
  
  List<String> getEmailInfo(Integer personId, Enabled enabled);

}
