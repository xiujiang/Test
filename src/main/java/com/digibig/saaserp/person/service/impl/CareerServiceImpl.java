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
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.digibig.saaserp.commons.exception.DBException;
import com.digibig.saaserp.person.domain.Career;
import com.digibig.saaserp.person.domain.CareerExample;
import com.digibig.saaserp.person.domain.CareerItem;
import com.digibig.saaserp.person.domain.CareerItemExample;
import com.digibig.saaserp.person.mapper.CareerItemMapper;
import com.digibig.saaserp.person.mapper.CareerMapper;
import com.digibig.saaserp.person.service.CareerService;
import com.digibig.saaserp.person.utils.Enabled;

@Service
public class CareerServiceImpl implements CareerService {
  
  private Logger logger = LoggerFactory.getLogger(getClass());
  
  @Autowired
  private CareerMapper careerMapper;
  
  @Autowired
  private CareerItemMapper careerItemMapper;

  /**
   * 添加工作经历
   */
  @Transactional
  @Override
  @Caching(evict = {
      @CacheEvict(value = "career",
          key = "'com.digibig.saaserp.person.domain.career_person_id_'+#career.getPersonId()")})
  public Integer addCareer(Career career){
    
    //通过自然人id，开始日期，公司全名验重
    Career aCareer = getCareer(career.getPersonId(),career.getStartDate(),career.getCompanyFullName());
    
    if(aCareer == null) {
      
      try {
        careerMapper.insertSelective(career);
      }catch(RuntimeException e) {
        logger.error("数据库操作异常",e);
        throw new DBException("数据库操作异常",e);
      }
      
      return career.getId();
    }else {
      //存在,判断是否有效，无效则设置为有效状态
      if(aCareer.getEnabled() == Enabled.NOT_ENABLED.getValue()) {
        
        this.setCareerEnable(aCareer.getPersonId(), aCareer.getId(), Enabled.ENABLED);
        
        return aCareer.getId();
      }
      return null;
    }
  }

  /**
   * 修改工作经历
   */
  @Transactional
  @Override
  @Caching(evict = {
      @CacheEvict(value = "career",
          key = "'com.digibig.saaserp.person.domain.career_person_id_'+#career.getPersonId()")})
  public Boolean setCareer(Career career) {
    CareerExample example = new CareerExample();
    example.createCriteria().andIdEqualTo(career.getId()).andPersonIdEqualTo(career.getPersonId());
    
    Integer rows = null;
    try {
      rows = careerMapper.updateByExampleSelective(career, example);
    }catch(RuntimeException e) {
      logger.error("数据库操作异常",e);
      throw new DBException("数据库操作异常",e);
    }

    return rows>0;
  }
  
  /**
   * 查询自然人工作经历信息
   */
  @Override
  @Cacheable(value = "career", key = "'com.digibig.saaserp.person.domain.career_person_id_'+#personId")
  public List<Map<String ,Object>> getCareers(Integer personId) {
    return careerMapper.getCareersByPersonId(personId,Enabled.ENABLED.getValue());
  }
  
  /**
   * 修改工作经历有效性
   */
  @Transactional
  @Override
  @Caching(evict = {
      @CacheEvict(value = "career",
          key = "'com.digibig.saaserp.person.domain.career_person_id_'+#personId")})
  public Boolean setCareerEnable(Integer personId, Integer careerId, Enabled enabled){
    
    CareerExample example = new CareerExample();
    example.createCriteria().andIdEqualTo(careerId).andPersonIdEqualTo(personId);
    
    Career career = new Career();
    career.setEnabled(enabled.getValue());
    
    Integer rows = null;
    try {
      rows = careerMapper.updateByExampleSelective(career, example);
    }catch(RuntimeException e) {
      logger.error("数据库操作异常",e);
      throw new DBException("数据库操作异常",e);
    }

    return rows>0;
  }

  /**
   * 添加工作详情
   */
  @Transactional
  @Override
  @Caching(evict = {
      @CacheEvict(value = "career",
          key = "'com.digibig.saaserp.person.domain.career_person_id_'+#careerItem.getPersonId()")})
  public Integer addCareerItem(CareerItem careerItem) {

    Career career = careerMapper.selectByPrimaryKey(careerItem.getCareerId());
    if(career != null && Enabled.ENABLED.getValue() == career.getEnabled()) {

      try {
        careerItemMapper.insertSelective(careerItem);
      }catch(RuntimeException e) {
        logger.error("数据库操作异常",e);
        throw new DBException("数据库操作异常",e);
      }
      
      return careerItem.getId();
    }else {
      return null;
    }
  }

  /**
   * 修改工作详情
   */
  @Transactional
  @Override
  @Caching(evict = {
      @CacheEvict(value = "career",
          key = "'com.digibig.saaserp.person.domain.career_person_id_'+#careerItem.getPersonId()")})
  public Boolean setCareerItem(CareerItem careerItem){
    
    CareerItemExample example = new CareerItemExample();
    example.createCriteria().andIdEqualTo(careerItem.getId())
            .andCareerIdEqualTo(careerItem.getCareerId()).andPersonIdEqualTo(careerItem.getPersonId());

    Integer rows = null;
    try {
      rows = careerItemMapper.updateByExampleSelective(careerItem, example);
    }catch(RuntimeException e) {
      logger.error("数据库操作异常",e);
      throw new DBException("数据库操作异常",e);
    }

    return rows>0;
  }

  /**
   * 设置工作详情的有效性
   */
  @Transactional
  @Override
  @Caching(evict = {
      @CacheEvict(value = "career",
          key = "'com.digibig.saaserp.person.domain.career_person_id_'+#personId")})
  public Boolean setItemEnabled(Integer personId, Integer careerId, Integer careerItemId,
      Enabled enabled) {
    CareerItemExample example = new CareerItemExample();
    example.createCriteria().andIdEqualTo(careerItemId).andCareerIdEqualTo(careerId).andPersonIdEqualTo(personId);
    
    CareerItem careerItem = new CareerItem();
    careerItem.setEnabled(enabled.getValue());
    careerItem.setLastTime(new Date());
    
    Integer rows = null;
    try {
      rows = careerItemMapper.updateByExampleSelective(careerItem, example);
    }catch(RuntimeException e) {
      logger.error("数据库操作异常",e);
      throw new DBException("数据库操作异常",e);
    }
    return rows>0;
  }
  

  /**
   * 通过自然人id，开始日期，公司名查找职业经历信息
   * @param personId 自然人id
   * @param startDate 开始日期
   * @param companyFullName 公司名
   * @return 职业经历
   */
  private Career getCareer(Integer personId,Date startDate, String companyFullName) {
    CareerExample example = new CareerExample();
    example.createCriteria().andPersonIdEqualTo(personId)
           .andStartDateEqualTo(startDate).andCompanyFullNameEqualTo(companyFullName);
    List<Career> careers = careerMapper.selectByExample(example);
    if(careers.size() == 0) {
      return null;
    }
    return careers.get(0);
  } 
  
}
