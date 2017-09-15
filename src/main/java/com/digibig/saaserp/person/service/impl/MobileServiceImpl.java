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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.digibig.saaserp.commons.exception.DBException;
import com.digibig.saaserp.commons.util.MaskedUtil;
import com.digibig.saaserp.commons.util.RegexValidator;
import com.digibig.saaserp.person.common.CommonParam;
import com.digibig.saaserp.person.domain.Mobile;
import com.digibig.saaserp.person.domain.MobileExample;
import com.digibig.saaserp.person.mapper.MobileMapper;
import com.digibig.saaserp.person.service.MobileService;
import com.digibig.saaserp.person.service.PersonService;
import com.digibig.saaserp.person.utils.Enabled;

@Service
public class MobileServiceImpl implements MobileService {
  
  private Logger logger = LoggerFactory.getLogger(getClass());
  
  @Autowired
  private MobileMapper mobileMapper;
  
  @Autowired
  private PersonService personService;
  

  /**
   * 添加自然人手机号
   */
  @Transactional
  @Override
  @Caching(evict = {
      @CacheEvict(value = "mobile",
          key = "'com.digibig.saaserp.person.domain.mobile_person_id_'+#personId"),
      @CacheEvict(value = "mobile",
          key = "'com.digibig.saaserp.person.domain.mobile_des_person_id_'+#personId")
      })
  public Integer addMobile(Integer personId, String mobileNumber, Boolean isDefault) {
    //检查自然人名下此手机号是否存在
    Mobile mobile = getMobileNum(personId,mobileNumber);
    Integer mobileId = null;
    //存在手机号时将有效性设置为有效，返回原记录id
    if(mobile == null) {
      mobileId = addMobile(personId, mobileNumber);
    }else {
      
      if(mobile.getEnabled() == Enabled.NOT_ENABLED.getValue()) {
        setEnabledById(mobile.getPersonId(), mobile.getId(), Enabled.ENABLED);
        mobileId = mobile.getId();
      }else {
        return null;
      }
    }
    
    //如需设置首选，设置首选     检查自然人当前是否有绑定手机号，无 -设置首选
    if(isDefault) {
      Boolean result = personService.setDefaultMobile(personId, mobileId);
      if(!result) {
        logger.error("设置默认手机号失败");
        return null;
      }
    }
    return mobileId;
  }

  
  /**
   * 设置手机号有效性状态
   */
  @Transactional
  @Override
  @Caching(evict = {
      @CacheEvict(value = "mobile",
          key = "'com.digibig.saaserp.person.domain.mobile_person_id_'+#personId"),
      @CacheEvict(value = "mobile",
          key = "'com.digibig.saaserp.person.domain.mobile_des_person_id_'+#personId")
      })
  public Boolean setMobileEnabled(Integer personId, String number, Enabled enabled) {
    
    if(RegexValidator.isMobile(number)) {
      return setEnabledByNum(personId,number,enabled);
    }else {
      Integer id = Integer.valueOf(number);
      return setEnabledById(personId,id,enabled);
    }
  }

  /**
   * 查询自然人手机号信息 - 脱敏
   */
  @Override
  @Cacheable(value = "mobile", key = "'com.digibig.saaserp.person.domain.mobile_des_person_id_'+#personId")
  public List<String> getDesensitizeInfo(Integer personId, Enabled enabled) {
    //查询数据
    List<String> numbers = mobileMapper.getMobiles(personId, enabled.getValue());
    //脱敏处理
    Integer index = 0;
    for(String num : numbers) {
      String number = MaskedUtil.masked(num, CommonParam.MOBILE_DES_PRE, CommonParam.MOBILE_DES_SUF);
      numbers.set(index, number);
      index++;
    }
    return numbers;
  }

  /**
   * 查询自然人手机号信息 - 不脱敏
   */
  @Override
  @Cacheable(value = "mobile", key = "'com.digibig.saaserp.person.domain.mobile_person_id_'+#personId")
  public List<String> getMobileInfo(Integer personId, Enabled enabled) {
    return mobileMapper.getMobiles(personId, enabled.getValue());
  }
  
  /**
   * 根据personId和手机号查询手机
   * @param personId 自然人id
   * @param mobileNumber 手机号
   * @return 存在手机号时返回该条记录id，否则返回null
   */
  private Mobile getMobileNum(Integer personId, String mobileNumber) {
    MobileExample example = new MobileExample();
    example.createCriteria().andPersonIdEqualTo(personId).andNumberEqualTo(mobileNumber);
    List<Mobile> mobiles = mobileMapper.selectByExample(example);
    if(mobiles.size() == 0) {
      return null;
    }
    return mobiles.get(0);
  }
  
  
  /**
   * 添加手机，默认有效
   * @param personId 自然人id
   * @param mobileNumber 手机号
   * @return id
   */
  private Integer addMobile(Integer personId, String mobileNumber) {
    Mobile mobile = new Mobile();
    mobile.setPersonId(personId);
    mobile.setNumber(mobileNumber);
    mobile.setCreateTime(new Date());
    mobile.setLastTime(new Date());
    try {
      mobileMapper.insertSelective(mobile);
    }catch(RuntimeException e) {
      logger.error("数据库操作异常",e);
      throw new DBException("数据库操作异常",e);
    }
    
    return mobile.getId();
  }
  
  
  /**
   * 通过自然人id和手机id设置手机有效性
   * @param personId 自然人id
   * @param id 手机id
   * @param enabled 有效性状态
   * @return 操作结果
   */
  private Boolean setEnabledById(Integer personId, Integer id, Enabled enabled) {
    
    MobileExample example = new MobileExample();
    example.createCriteria().andIdEqualTo(id).andPersonIdEqualTo(personId);
    
    Mobile mobile = new Mobile();
    mobile.setEnabled(enabled.getValue());
    
    Integer rows = null;
    try {
      rows = mobileMapper.updateByExampleSelective(mobile, example);
    }catch(RuntimeException e) {
      logger.error("数据库操作异常",e);
      throw new DBException("数据库操作异常",e);
    }
    if(Enabled.NOT_ENABLED.getValue() == enabled.getValue()) {
      personService.delDefaultMobile(personId, id);
    }
    return rows>0;
  }
  
  /**
   * 通过自然人id和手机号码设置手机的有效性
   * @param personId 自然人id
   * @param number 电话号码
   * @param enabled 有效性状态
   * @return 操作结果
   */
  private Boolean setEnabledByNum(Integer personId, String number, Enabled enabled) {
      MobileExample example = new MobileExample();
      example.createCriteria().andPersonIdEqualTo(personId).andNumberEqualTo(number);
      
      Mobile mobile = new Mobile();
      mobile.setEnabled(enabled.getValue());
      
      Integer rows = null; 
      try {
        rows = mobileMapper.updateByExampleSelective(mobile, example);
      }catch(RuntimeException e) {
        logger.error("数据库操作异常",e);
        throw new DBException("数据库操作异常",e);
      }
      if(Enabled.NOT_ENABLED.getValue() == enabled.getValue()) {
        List<Mobile> mobiles = mobileMapper.selectByExample(example);
        if(mobiles.size() != 0) {
          personService.delDefaultMobile(personId, mobiles.get(0).getId());
        }
      }
      return rows>0;
  }


  /*
   * 获取手机号
   */
  @Override
  public Mobile getMobile(Integer personId, String mobile) {
    MobileExample example = new MobileExample();
    example.createCriteria().andPersonIdEqualTo(personId)
    .andNumberEqualTo(mobile).andEnabledEqualTo(Enabled.ENABLED.getValue());
    
    List<Mobile> mobiles = mobileMapper.selectByExample(example);
    if(mobiles.size() == 0) {
      return null;
    }else {
      return mobiles.get(0);
    }
  }

}
