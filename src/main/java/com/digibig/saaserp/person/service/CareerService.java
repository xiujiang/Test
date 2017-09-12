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

import com.digibig.saaserp.person.domain.Career;
import com.digibig.saaserp.person.domain.CareerItem;
import com.digibig.saaserp.person.utils.Enabled;

public interface CareerService {
  
  /**
   * 添加工作经历
   * @param career 职业经历
   * @return 工作经历id
   */
  Integer addCareer(Career career) ;
  
  /**
   * 修改工作经历
   * @param career 职业经历
   * @return 操作结果
   */
  Boolean setCareer(Career career) ;
  
  /**
   * 设置工作经历的有效性
   * @param personId 自然人id
   * @param careerId 工作经历id
   * @param enabled 有效性
   * @return 操作结果
   */
  Boolean setCareerEnable(Integer personId,Integer careerId,Enabled enabled);
  
  /**
   * 添加工作详情
   * @param careerItem 职业详情
   * @return 详情id
   */
  Integer addCareerItem(CareerItem careerItem);
  
  /**
   * 修改工作详情
   * @param careerItem 工作详情
   * @return 操作结果
   */
  Boolean setCareerItem(CareerItem careerItem) ;
  
  /**
   * 设置详情有效性
   * @param personId 自然人id
   * @param careerId 工作经历id
   * @param careerItemId 详情id
   * @param enabled 详情有效性
   * @return 操作结果
   */
  Boolean setItemEnabled(Integer personId,Integer careerId,Integer careerItemId,Enabled enabled);
  
  /**
   * 查询工作经历
   * @param personId 自然人id
   * @return 工作经历信息
   */
  List<Map<String ,Object>> getCareers(Integer personId);

}
