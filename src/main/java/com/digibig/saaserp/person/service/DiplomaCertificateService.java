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

import com.digibig.saaserp.person.domain.DiplomaCertificate;
import com.digibig.saaserp.person.domain.SchoolRecord;
import com.digibig.saaserp.person.utils.Enabled;

public interface DiplomaCertificateService {
  
  /**
   * 添加学位证
   * @param diploma 学位证
   * @return 学位证id
   */
  Integer addDiplomaCertificate(DiplomaCertificate diploma) ;
  
  /**
   * 修改学位证
   * @param diploma 学位证
   * @return 操作结果
   */
  Boolean setDiplomaCertificate(DiplomaCertificate diploma) ;
  
  /**
   * 设置学位证有效性
   * @param personId 自然人id
   * @param certificateId 学位证id
   * @param enabled 有效性
   * @return 操作结果
   */
  Boolean setEnabled(Integer personId,Integer certificateId,Enabled enabled) ;
  
  /**
   * 添加学历证
   * @param record 学历证
   * @return 学历证id
   */
  Integer addSchoolRecord(SchoolRecord record) ;
  
  
  /**
   * 修改学历证
   * @param record 学历证
   * @return 操作结果
   */
  Boolean setSchoolRecord(SchoolRecord record) ;
    
  /**
   * 设置学历证有效性
   * @param personId 自然人id
   * @param recordId 学历证id
   * @param enabled 有效性
   * @return 操作结果
   */
  Boolean setRecordEnabled(Integer personId,Integer recordId,Enabled enabled) ;
  
  /**
   * 添加认证报告
   * @param personId 自然人id
   * @param recordId 学历id
   * @param reportFile 认证文件id
   * @return 操作结果
   */
  Boolean addCheckReport(Integer personId,Integer recordId,Integer reportFile) ;
  
  /**
   * 获取学历信息
   * @param personId 自然人id
   * @return 自然人信息
   */
  List<Map<String, Object>> getSchoolRecord(Integer personId);

}
