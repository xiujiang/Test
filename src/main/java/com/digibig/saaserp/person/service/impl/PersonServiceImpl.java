/*
 * Copyright 2017 - 数多科技
 * 
 * 北京数多信息科技有限公司
 * 本公司保留所有下述内容的权利。
 * 本内容为保密信息，仅限本公司内部使用。
 * 非经本公司书面许可，任何人不得外泄或用于其他目的。
 */
package com.digibig.saaserp.person.service.impl;

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
import com.digibig.saaserp.commons.exception.DigibigException;
import com.digibig.saaserp.commons.util.MaskedUtil;
import com.digibig.saaserp.commons.util.RegexValidator;
import com.digibig.saaserp.person.common.CommonParam;
import com.digibig.saaserp.person.domain.Address;
import com.digibig.saaserp.person.domain.AddressExample;
import com.digibig.saaserp.person.domain.Email;
import com.digibig.saaserp.person.domain.EmailExample;
import com.digibig.saaserp.person.domain.Mobile;
import com.digibig.saaserp.person.domain.Person;
import com.digibig.saaserp.person.domain.PersonExample;
import com.digibig.saaserp.person.mapper.AddressMapper;
import com.digibig.saaserp.person.mapper.EmailMapper;
import com.digibig.saaserp.person.mapper.MobileMapper;
import com.digibig.saaserp.person.mapper.PersonMapper;
import com.digibig.saaserp.person.service.AddressService;
import com.digibig.saaserp.person.service.MobileService;
import com.digibig.saaserp.person.service.PersonService;
import com.digibig.saaserp.person.utils.Enabled;
import com.digibig.saaserp.person.utils.IDCardType;

import java.util.List;

@Service
public class PersonServiceImpl implements PersonService {
  
  private Logger logger = LoggerFactory.getLogger(getClass());
  
  @Autowired
  private PersonMapper personMapper;
  
  @Autowired
  private MobileService mobileService;
  
  @Autowired
  private MobileMapper mobileMapper;
  
  @Autowired
  private AddressService addressService;
  
  @Autowired
  private AddressMapper addressMapper;
  
  @Autowired
  private EmailMapper emailMapper;

  
  @Override
  @Cacheable(value = "person", key = "'com.digibig.saaserp.person.domain.person_mobile_person_id_'+#personId")
  public Integer getDefaultMobile(Integer personId) {
    return personMapper.getdefaultMobile(personId);
  }


  @Override
  @Cacheable(value = "person", key = "'com.digibig.saaserp.person.domain.person_email_person_id_'+#personId")
  public Integer getDefaultEmail(Integer personId) {
    return personMapper.getdefaultEmail(personId);
  }

  /*
   * 设置默认邮箱
   */
  @Transactional
  @Override
  @Caching(evict = {
      @CacheEvict(value = "person",
          key = "'com.digibig.saaserp.person.domain.person_person_id_'+#personId"),
      @CacheEvict(value = "person",
          key = "'com.digibig.saaserp.person.domain.person_person_des_id_'+#personId"),
      @CacheEvict(value = "person",
          key = "'com.digibig.saaserp.person.domain.person_email_person_id_'+#personId"),
      })
  public Boolean setDefaultEmail(Integer personId, Integer emailId, Boolean realEmail) {
    
    //不确认邮箱是否有效时查看地址是否有效
    if(!realEmail) {
      EmailExample example = new EmailExample();
      example.createCriteria().andIdEqualTo(emailId).andEnabledEqualTo(Enabled.ENABLED.getValue());
      List<Email> emails = emailMapper.selectByExample(example);
      if(emails.size() == 0) {
        return false;
      }
    }
    
    Person person = new Person();
    person.setId(personId);
    person.setDefaultEmail(emailId);
    
    Integer rows = null;
    
    try {
      rows = personMapper.updateByPrimaryKeySelective(person);
    }catch(RuntimeException e) {
      logger.error("数据库操作异常",e);
      throw new DBException("数据库操作异常",e);
    }
    
    return rows>0;
  }

  /*
   * 身份认证
   */
  @Transactional
  @Override
  public Integer identityVerificate(String IDCard, String name) {
    //根据身份证号和姓名查询自然人
    PersonExample example = new PersonExample();
    example.createCriteria().andIdNumberEqualTo(IDCard).andNameEqualTo(name);
    
    List<Person> idCards = personMapper.selectByExample(example);
    //有则返回id，否则调用第三方接口
    if(idCards.size() != 0) {
      return idCards.get(0).getId();
    }
    
    //TODO 调用第三方接口查询
    Boolean result = true;
    
    //合法，在数据库中添加自然对象
    if(result) {
      Person person = new Person();
      person.setIdType(IDCardType.SECOND.getValue());
      person.setName(name);
      person.setIdNumber(IDCard);
      
      try {
        personMapper.insertSelective(person);
      }catch(RuntimeException e) {
        logger.error("数据库操作异常",e);
        throw new DBException("数据库操作异常",e);
      }
      
      return person.getId();
    }
    //不合法，返回null
    return null;
  }

  /*
   * 清除默认邮箱
   */
  @Transactional
  @Override
  @Caching(evict = {
      @CacheEvict(value = "person",
          key = "'com.digibig.saaserp.person.domain.person_person_id_'+#personId"),
      @CacheEvict(value = "person",
          key = "'com.digibig.saaserp.person.domain.person_person_des_id_'+#personId"),
      @CacheEvict(value = "person",
          key = "'com.digibig.saaserp.person.domain.person_email_person_id_'+#personId"),
      })
  public Boolean delDefaultEmail(Integer personId) {
    Person person = new Person();
    person.setId(personId);
    person.setDefaultEmail(CommonParam.DEFAULT_INT);
    
    Integer rows = null;
    try {
      rows = personMapper.updateByPrimaryKeySelective(person);
    }catch(RuntimeException e) {
      logger.error("数据库操作异常",e);
      throw new DBException("数据库操作异常",e);
    }
    return rows>0;
  }

  /*
   * 通过身份证号查询自然人信息
   */
  @Override
  public Map<String, Object> getByCardNumber(String idCard) throws DigibigException {
    
    Map<String, Object> map = personMapper.getByCardNumber(idCard);
    //脱敏处理
    map.put("mobile", MaskedUtil.masked((String)map.get("mobile"), CommonParam.MOBILE_DES_PRE, CommonParam.MOBILE_DES_SUF));
    map.put("idCard", MaskedUtil.masked((String)map.get("idCard"), CommonParam.IDCARD_DES_PRE, CommonParam.IDCARD_DES_SUF));
    String email = (String)map.get("email");
    Integer suf = email.length() - email.lastIndexOf("@");
    map.put("email", MaskedUtil.masked(email, CommonParam.EMIAL_DES_PRE, suf));
    // 查询地址信息
    map.get("addressId");
    Integer node = (Integer)map.get("addressId");
    if(node == CommonParam.DEFAULT_INT) {
      map.put("address", "");
    }else {
      map.put("address", addressService.getAddressById(node));
    }
    map.remove("addressId");
    return map;
  }

  /*
   * 清除默认手机
   */
  @Transactional
  @Override
  @Caching(evict = {
      @CacheEvict(value = "person",
          key = "'com.digibig.saaserp.person.domain.person_person_id_'+#personId"),
      @CacheEvict(value = "person",
          key = "'com.digibig.saaserp.person.domain.person_person_des_id_'+#personId"),
      @CacheEvict(value = "person", 
          key = "'com.digibig.saaserp.person.domain.person_mobile_person_id_'+#personId")
      })
  public Boolean delDefaultMobile(Integer personId) {
    Person person = new Person();
    person.setId(personId);
    person.setDefaultMobile(CommonParam.DEFAULT_INT);
    
    Integer rows = null;
    try {
      rows = personMapper.updateByPrimaryKeySelective(person);
    }catch(RuntimeException e) {
      logger.error("数据库操作异常",e);
      throw new DBException("数据库操作异常",e);
    }

    return rows>0;
  }

  /*
   * 清除默认地址
   */
  @Transactional
  @Override
  @Caching(evict = {
      @CacheEvict(value = "person",
          key = "'com.digibig.saaserp.person.domain.person_person_id_'+#personId"),
      @CacheEvict(value = "person",
          key = "'com.digibig.saaserp.person.domain.person_person_des_id_'+#personId"),
      @CacheEvict(value = "person",
          key = "'com.digibig.saaserp.person.domain.person_address_person_id_'+#personId"),
      })
  public Boolean delDefaultAddress(Integer personId) {
    Person person = new Person();
    person.setId(personId);
    person.setDefaultAddress(CommonParam.DEFAULT_INT);
    
    Integer rows = null;
    try {
      rows = personMapper.updateByPrimaryKeySelective(person);
    }catch(RuntimeException e) {
      logger.error("数据库操作异常",e);
      throw new DBException("数据库操作异常",e);
    }

    return rows>0;
  }

  /*
   * 获取自然人脱敏信息
   */
  @Override
  @Cacheable(value = "person", key = "'com.digibig.saaserp.person.domain.person_des_person_id_'+#personId")
  public Map<String, Object> getDesensitizeInfo(Integer personId) throws DigibigException {
    
    Map<String, Object> map = personMapper.getPersonById(personId);
    //脱敏处理
    map.put("mobile", MaskedUtil.masked((String)map.get("mobile"), CommonParam.MOBILE_DES_PRE, CommonParam.MOBILE_DES_SUF));
    map.put("idCard", MaskedUtil.masked((String)map.get("idCard"), CommonParam.IDCARD_DES_PRE, CommonParam.IDCARD_DES_SUF));
    String email = (String)map.get("email");
    Integer suf = email.length() - email.lastIndexOf("@");
    map.put("email", MaskedUtil.masked(email, CommonParam.EMIAL_DES_PRE, suf));
    // 查询地址信息detailAddress
    Integer node = (Integer)map.get("addressId");
    if(node == CommonParam.DEFAULT_INT) {
      map.put("address", "");
    }else {
      map.put("address", addressService.getAddressById(node));
    }
    map.remove("addressId");
    return map;
  }

  /*
   * 获取自然人信息
   */
  @Override
  @Cacheable(value = "person", key = "'com.digibig.saaserp.person.domain.person_person_id_'+#personId")
  public Map<String, Object> getPersonInfo(Integer personId) throws DigibigException{
    Map<String, Object> map = personMapper.getPersonById(personId);
    //查询地址信息
    Integer node = (Integer)map.get("addressId");
    if(node == CommonParam.DEFAULT_INT) {
      map.put("address", "");
    }else {
      map.put("address", addressService.getAddressById(node));
    }
    map.remove("addressId");
    return map;
  }
  

  /*
   * 设置默认手机
   */
  @Transactional
  @Override
  @Caching(evict = {
      @CacheEvict(value = "person",
          key = "'com.digibig.saaserp.person.domain.person_person_id_'+#personId"),
      @CacheEvict(value = "person",
          key = "'com.digibig.saaserp.person.domain.person_person_des_id_'+#personId"),
      @CacheEvict(value = "person", 
          key = "'com.digibig.saaserp.person.domain.person_mobile_person_id_'+#personId")})
  public Boolean setDefaultMobile(Integer personId, Integer mobileId) {
    Person person = new Person();
    person.setId(personId);
    person.setDefaultMobile(mobileId);
    
    Integer rows = null;
    try {
      rows = personMapper.updateByPrimaryKeySelective(person);
    }catch(RuntimeException e) {
      logger.error("数据库操作异常",e);
      throw new DBException("数据库操作异常",e);
    }
    
    return rows>0;
  }

  /*
   * 设置默认手机
   */
  @Transactional
  @Override
  public Boolean setDefaultMobile(Integer personId, String mobile) {
    
    if(RegexValidator.isMobile(mobile)) {
      Mobile aMobile = mobileService.getMobile(personId, mobile);
      
      if(aMobile == null) {
        return false;
      }
      return setDefaultMobile(personId,aMobile.getId());
    }else {
      
      Integer mobileId = Integer.valueOf(mobile);
      Mobile aMobile = mobileMapper.selectByPrimaryKey(mobileId);
      
      if(aMobile == null) {
        return false;
      }
      return setDefaultMobile(personId,mobileId);
    }
  }

  /*
   * 设置默认地址
   */
  @Transactional
  @Override
  @Caching(evict = {
      @CacheEvict(value = "person",
          key = "'com.digibig.saaserp.person.domain.person_person_id_'+#personId"),
      @CacheEvict(value = "person",
          key = "'com.digibig.saaserp.person.domain.person_person_des_id_'+#personId"),
      @CacheEvict(value = "person",
          key = "'com.digibig.saaserp.person.domain.person_address_person_id_'+#personId"),
      })
  public Boolean setDefaultAddress(Integer personId, Integer addressId, Boolean realAddress) {
    
    //不确认地址是否有效时查看地址是否有效
    if(!realAddress) {
      AddressExample example = new AddressExample();
      example.createCriteria().andIdEqualTo(addressId).andEnabledEqualTo(Enabled.ENABLED.getValue());
      List<Address> addrs = addressMapper.selectByExample(example);
      if(addrs.size() == 0) {
        return false;
      }
    }
    
    Person person = new Person();
    person.setId(personId);
    person.setDefaultAddress(addressId);
    
    Integer rows = null;
    
    try {
      rows = personMapper.updateByPrimaryKeySelective(person);
    }catch(RuntimeException e) {
      logger.error("数据库操作异常",e);
      throw new DBException("数据库操作异常",e);
    }
    return rows>0;
  }

  /*
   * 获取默认地址
   */
  @Override
  @Cacheable(value = "person", key = "'com.digibig.saaserp.person.domain.person_address_person_id_'+#personId")
  public Integer getdefaultAddress(Integer personId) {
    return personMapper.getdefaultAddress(personId);
  }

  /*
   * 获取默认身份证
   */
  @Override
  @Cacheable(value = "person", key = "'com.digibig.saaserp.person.domain.person_idcard_person_id_'+#personId")
  public Integer getdefaultIDCard(Integer personId) {
    return personMapper.getdefaultIDCard(personId);
  }

  /*
   * 设置默认身份证
   */
  @Transactional
  @Override
  @Caching(evict = {
      @CacheEvict(value = "person",
          key = "'com.digibig.saaserp.person.domain.person_person_id_'+#personId"),
      @CacheEvict(value = "person",
          key = "'com.digibig.saaserp.person.domain.person_person_des_id_'+#personId"),
      @CacheEvict(value = "person",
          key = "'com.digibig.saaserp.person.domain.person_idcard_person_id_'+#personId")
      })
  public Boolean setDefaultIDCard(Integer personId, Integer idCardId) {
    Person person = new Person();
    person.setId(personId);
    person.setDefaultIdCard(idCardId);
    
    Integer rows = null;
    try {
      rows = personMapper.updateByPrimaryKeySelective(person);
    }catch(RuntimeException e) {
      logger.error("数据库操作异常",e);
      throw new DBException("数据库操作异常",e);
    }

    return rows>0;
  }

  /*
   * 默认手机id为mobileId时清除默认手机
   */
  @Override
  @Caching(evict = {
      @CacheEvict(value = "person",
          key = "'com.digibig.saaserp.person.domain.person_person_id_'+#personId"),
      @CacheEvict(value = "person",
          key = "'com.digibig.saaserp.person.domain.person_person_des_id_'+#personId"),
      @CacheEvict(value = "person", 
          key = "'com.digibig.saaserp.person.domain.person_mobile_person_id_'+#personId")
      })
  public Boolean delDefaultMobile(Integer personId, Integer mobileId) {
    
    PersonExample example = new PersonExample();
    example.createCriteria().andIdEqualTo(personId).andDefaultMobileEqualTo(mobileId);
    
    Person person = new Person();
    person.setDefaultAddress(CommonParam.DEFAULT_INT);
    
    Integer rows = null;
    try {
      rows = personMapper.updateByExampleSelective(person, example);
    }catch(RuntimeException e) {
      logger.error("数据库操作异常",e);
      throw new DBException("数据库操作异常",e);
    }

    return rows>0;
  }

  /*
   * 默认邮箱id为emailId时清除默认邮箱
   */
  @Override
  @Caching(evict = {
      @CacheEvict(value = "person",
          key = "'com.digibig.saaserp.person.domain.person_person_id_'+#personId"),
      @CacheEvict(value = "person",
          key = "'com.digibig.saaserp.person.domain.person_person_des_id_'+#personId"),
      @CacheEvict(value = "person",
          key = "'com.digibig.saaserp.person.domain.person_email_person_id_'+#personId"),
      })
  public Boolean delDefaultEmail(Integer personId, Integer emailId) {
    
    PersonExample example = new PersonExample();
    example.createCriteria().andIdEqualTo(personId).andDefaultEmailEqualTo(emailId);
    
    Person person = new Person();
    person.setDefaultAddress(CommonParam.DEFAULT_INT);
    
    Integer rows = null;
    try {
      rows = personMapper.updateByExampleSelective(person, example);
    }catch(RuntimeException e) {
      logger.error("数据库操作异常",e);
      throw new DBException("数据库操作异常",e);
    }

    return rows>0;
  }

  /*
   * 默认地址id为addressId时清除默认地址
   */
  @Override
  @Caching(evict = {
      @CacheEvict(value = "person",
          key = "'com.digibig.saaserp.person.domain.person_person_id_'+#personId"),
      @CacheEvict(value = "person",
          key = "'com.digibig.saaserp.person.domain.person_person_des_id_'+#personId"),
      @CacheEvict(value = "person",
          key = "'com.digibig.saaserp.person.domain.person_address_person_id_'+#personId"),
      })
  public Boolean delDefaultAddress(Integer personId, Integer addressId) {
    
    PersonExample example = new PersonExample();
    example.createCriteria().andIdEqualTo(personId).andDefaultAddressEqualTo(addressId);
    
    Person person = new Person();
    person.setDefaultAddress(CommonParam.DEFAULT_INT);
    
    Integer rows = null;
    try {
      rows = personMapper.updateByExampleSelective(person, example);
    }catch(RuntimeException e) {
      logger.error("数据库操作异常",e);
      throw new DBException("数据库操作异常",e);
    }

    return rows>0;
  }

}
