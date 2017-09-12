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
        logger.error("设置默认地址失败");
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
  public Boolean setMobileEnabled(Integer personId, String number, Enabled enabled) {
    Integer result = null;
    
    if(RegexValidator.isMobile(number)) {
      result = setEnabledByNum(personId,number,enabled);
    }else {
      
      Integer id = Integer.valueOf(number);
      result = setEnabledById(personId,id,enabled);
    }
    if(result == 0) {
      return false;
    }
    return true;
  }

  /**
   * 查询自然人手机号信息 - 脱敏
   */
  @Override
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
  private Integer setEnabledById(Integer personId, Integer id, Enabled enabled) {
    
    MobileExample example = new MobileExample();
    example.createCriteria().andIdEqualTo(id).andPersonIdEqualTo(personId);
    
    Mobile mobile = new Mobile();
    mobile.setEnabled(enabled.getValue());
    
    Integer result = null;
    try {
      result = mobileMapper.updateByExampleSelective(mobile, example);
    }catch(RuntimeException e) {
      throw new DBException("数据库操作异常",e);
    }
    
    return result;
  }
  
  /**
   * 通过自然人id和手机号码设置手机的有效性
   * @param personId 自然人id
   * @param number 电话号码
   * @param enabled 有效性状态
   * @return 操作结果
   */
  private Integer setEnabledByNum(Integer personId, String number, Enabled enabled) {
      MobileExample example = new MobileExample();
      example.createCriteria().andPersonIdEqualTo(personId).andNumberEqualTo(number);
      
      Mobile mobile = new Mobile();
      mobile.setEnabled(enabled.getValue());
      
      Integer result = null; 
      try {
        result = mobileMapper.updateByExampleSelective(mobile, example);
      }catch(RuntimeException e) {
        throw new DBException("数据库操作异常",e);
      }
      
      return result;
  }


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
