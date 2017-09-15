/*
 * Copyright 2017 - 数多科技
 * 
 * 北京数多信息科技有限公司
 * 本公司保留所有下述内容的权利。
 * 本内容为保密信息，仅限本公司内部使用。
 * 非经本公司书面许可，任何人不得外泄或用于其他目的。
 */
package com.digibig.saaserp.person.service.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.digibig.saaserp.commons.exception.DBException;
import com.digibig.saaserp.person.domain.Education;
import com.digibig.saaserp.person.domain.EducationExample;
import com.digibig.saaserp.person.domain.EducationSummary;
import com.digibig.saaserp.person.domain.EducationSummaryExample;
import com.digibig.saaserp.person.mapper.EducationMapper;
import com.digibig.saaserp.person.mapper.EducationSummaryMapper;
import com.digibig.saaserp.person.service.EducationService;
import com.digibig.saaserp.person.utils.Enabled;

@Service
public class EducationServiceImpl implements EducationService {
  
  private Logger logger = LoggerFactory.getLogger(getClass());
  
  @Autowired
  private EducationMapper educationMapper;
  
  @Autowired
  private EducationSummaryMapper educationSummaryMapper;

  /*
   * 添加教育经历
   */
  @Transactional
  @Override
  public Integer addEducation(Education education) {

    try {
      educationMapper.insertSelective(education);
    }catch(RuntimeException e) {
      logger.error("数据库操作异常",e);
      throw new DBException("数据库操作异常");
    }
    return education.getId();
  }

  /*
   * 修改教育经历
   */
  @Transactional
  @Override
  public Boolean setEducation(Education education) {
    
    EducationExample example = new EducationExample();
    example.createCriteria().andIdEqualTo(education.getId()).andPersonIdEqualTo(education.getPersonId());
    
    Integer rows = null;
    try {
      rows = educationMapper.updateByExampleSelective(education, example);
    }catch(RuntimeException e) {
      logger.error("数据库操作异常",e);
      throw new DBException("数据库操作异常");
    }
    return rows>0;
  }

  /*
   * 设置教育经历有效性
   */
  @Transactional
  @Override
  public Boolean setEducationEnabled(Integer personId, Integer educationId, Enabled enabled){

    EducationExample example = new EducationExample();
    example.createCriteria().andIdEqualTo(educationId).andPersonIdEqualTo(personId);
    Education education = new Education();
    education.setEnabled(enabled.getValue());
    education.setLastTime(new Date());
    
    Integer rows = null;
    try {
      rows = educationMapper.updateByExampleSelective(education, example);
    }catch(RuntimeException e) {
      logger.error("数据库操作异常",e);
      throw new DBException("数据库操作异常");
    }
    return rows>0;
  }


  /**
   * 获取摘要信息
   * @param personId 自然人id
   * @return 摘要信息
   */
  private EducationSummary getSummary(Integer personId) {
    EducationSummaryExample example = new EducationSummaryExample();
    example.createCriteria().andPersonIdEqualTo(personId);
    
    List<EducationSummary> summarys = educationSummaryMapper.selectByExample(example);
    
    if(summarys.size() == 0) {
      return new EducationSummary();
    }

    return summarys.get(0);
  }

  /*
   * 修改教育摘要
   */
  @Transactional
  @Override
  public Integer setEducationSummary(EducationSummary educationSummary) {
    
    EducationSummary summary = this.getSummary(educationSummary.getPersonId());
    educationSummary.setId(summary.getId());
      
    try {
      if(educationSummary.getId() == null) {
        educationSummaryMapper.insertSelective(educationSummary);
      }else {
        educationSummaryMapper.updateByPrimaryKeySelective(educationSummary);
      }
    }catch(RuntimeException e) {
      logger.error("数据库操作异常",e);
      throw new DBException("数据库操作异常");
    }
    
    return educationSummary.getId();
  }
}
