/*
 * Copyright 2017 - 数多科技
 * 
 * 北京数多信息科技有限公司
 * 本公司保留所有下述内容的权利。
 * 本内容为保密信息，仅限本公司内部使用。
 * 非经本公司书面许可，任何人不得外泄或用于其他目的。
 */
package com.digibig.saaserp.person.service;

import com.digibig.saaserp.person.domain.Education;
import com.digibig.saaserp.person.domain.EducationSummary;
import com.digibig.saaserp.person.utils.Enabled;

public interface EducationService {
  
  /**
   * 添加教育经历
   * @param education 教育经历
   * @return 教育经历id
   */
  Integer addEducation(Education education);
  
  /**
   * 修改教育经历
   * @param education 教育经历
   * @return 操作结果
   */
  Boolean setEducation(Education education) ;
  
  /**
   * 设置教育经历有效性
   * @param personId 自然人id
   * @param educationId 教育经历id
   * @param enabled 有效性
   * @return 操作结果
   */
  Boolean setEducationEnabled(Integer personId, Integer educationId, Enabled enabled) ;
  
  /**
   * 操作教育摘要
   * @param educationSummary 教育摘要
   * @return 教育摘要id
   */
  Integer setEducationSummary(EducationSummary educationSummary) ;

}
