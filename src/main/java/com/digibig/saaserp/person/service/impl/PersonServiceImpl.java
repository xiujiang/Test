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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.digibig.saaserp.commons.exception.DBException;
import com.digibig.saaserp.commons.exception.DigibigException;
import com.digibig.saaserp.commons.util.MaskedUtil;
import com.digibig.saaserp.commons.util.RegexValidator;
import com.digibig.saaserp.person.common.CommonParam;
import com.digibig.saaserp.person.domain.Address;
import com.digibig.saaserp.person.domain.Mobile;
import com.digibig.saaserp.person.domain.Person;
import com.digibig.saaserp.person.domain.PersonExample;
import com.digibig.saaserp.person.mapper.AddressMapper;
import com.digibig.saaserp.person.mapper.MobileMapper;
import com.digibig.saaserp.person.mapper.PersonMapper;
import com.digibig.saaserp.person.service.AddressService;
import com.digibig.saaserp.person.service.MobileService;
import com.digibig.saaserp.person.service.PersonService;
import com.digibig.saaserp.person.utils.IDCardType;

import java.util.Date;
import java.util.List;

@Service
public class PersonServiceImpl implements PersonService {
  
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

  @Override
  public Integer getDefaultMobile(Integer personId) {
    
    return personMapper.getdefaultMobile(personId);
  }

  @Transactional
  @Override
  public Boolean setDefaultMobile(Integer personId, Integer mobileId) {
    Person person = new Person();
    person.setId(personId);
    person.setDefaultMobile(mobileId);
    person.setLastTime(new Date());
    
    Integer rows = null;
    try {
      rows = personMapper.updateByPrimaryKeySelective(person);
    }catch(RuntimeException e) {
      throw new DBException("数据库操作异常",e);
    }
    
    if(rows == 0) {
      return false;
    }
    return true;
  }

  @Override
  public Integer getDefaultEmail(Integer personId) {
    
    return personMapper.getdefaultEmail(personId);
  }

  
  @Transactional
  @Override
  public Boolean setDefaultEmail(Integer personId, Integer emailId) {
    
    Person person = new Person();
    person.setId(personId);
    person.setDefaultEmail(emailId);
    person.setLastTime(new Date());
    
    Integer rows = null;
    try {
      rows = personMapper.updateByPrimaryKeySelective(person);
    }catch(RuntimeException e) {
      throw new DBException("数据库操作异常",e);
    }
    if(rows == 0) {
      return false;
    }
    return true;
  }

  
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
      person.setCreateTime(new Date());
      person.setLastTime(new Date());
      
      try {
        personMapper.insertSelective(person);
      }catch(RuntimeException e) {
        throw new DBException("数据库操作异常",e);
      }
      
      return person.getId();
    }
    //不合法，返回null
    return null;
  }

  @Transactional
  @Override
  public Boolean delDefaultEmail(Integer personId) {
    Person person = new Person();
    person.setId(personId);
    person.setDefaultEmail(CommonParam.DEFAULT_INT);
    person.setLastTime(new Date());
    
    Integer rows = null;
    try {
      rows = personMapper.updateByPrimaryKeySelective(person);
    }catch(RuntimeException e) {
      throw new DBException("数据库操作异常",e);
    }
    
    if(rows == 0) {
      return false;
    }
    return true;
  }

  @Override
  public Map<String, String> getByCardNumber(String IDCard) throws DigibigException {
    Map<String, String> map = personMapper.getByCardNumber(IDCard);
    //脱敏处理
    map.put("mobile", MaskedUtil.masked(map.get("mobile"), CommonParam.MOBILE_DES_PRE, CommonParam.MOBILE_DES_SUF));
    map.put("idCard", MaskedUtil.masked(map.get("idCard"), CommonParam.IDCARD_DES_PRE, CommonParam.IDCARD_DES_SUF));
    String email = map.get("email");
    Integer suf = email.length() - email.lastIndexOf("@");
    map.put("email", MaskedUtil.masked(email, CommonParam.EMIAL_DES_PRE, suf));
    // 查询地址信息
    String lastNode = map.get("addressId");
    Integer node = Integer.valueOf(lastNode);
    if(node == CommonParam.DEFAULT_INT) {
      map.put("address", "");
    }else {
      map.put("address", addressService.getAddressById(node));
    }
    map.remove("addressId");
    return map;
  }

  
  @Transactional
  @Override
  public Boolean delDefaultMobile(Integer personId) {
    Person person = new Person();
    person.setId(personId);
    person.setDefaultMobile(CommonParam.DEFAULT_INT);
    person.setLastTime(new Date());
    
    Integer rows = null;
    try {
      rows = personMapper.updateByPrimaryKeySelective(person);
    }catch(RuntimeException e) {
      throw new DBException("数据库操作异常",e);
    }
    
    if(rows == 0) {
      return false;
    }
    return true;
  }

  
  @Transactional
  @Override
  public Boolean delDefaultAddress(Integer personId) {
    Person person = new Person();
    person.setId(personId);
    person.setDefaultAddress(CommonParam.DEFAULT_INT);
    person.setLastTime(new Date());
    
    Integer rows = null;
    try {
      rows = personMapper.updateByPrimaryKeySelective(person);
    }catch(RuntimeException e) {
      throw new DBException("数据库操作异常",e);
    }
    
    if(rows == 0) {
      return false;
    }
    return true;
  }

  @Override
  public Map<String, String> getDesensitizeInfo(Integer personId) throws DigibigException {
    
    Map<String, String> map = personMapper.getPersonById(personId);
    //脱敏处理
    map.put("mobile", MaskedUtil.masked(map.get("mobile"), CommonParam.MOBILE_DES_PRE, CommonParam.MOBILE_DES_SUF));
    map.put("idCard", MaskedUtil.masked(map.get("idCard"), CommonParam.IDCARD_DES_PRE, CommonParam.IDCARD_DES_SUF));
    String email = map.get("email");
    int suf = email.length() - email.lastIndexOf("@");
    map.put("email", MaskedUtil.masked(email, CommonParam.EMIAL_DES_PRE, suf));
    // 查询地址信息detailAddress
    Integer node = Integer.valueOf(map.get("addressId"));
    if(node == CommonParam.DEFAULT_INT) {
      map.put("address", "");
    }else {
      map.put("address", addressService.getAddressById(node));
    }
    map.remove("addressId");
    return map;
  }

  @Override
  public Map<String, String> getPersonInfo(Integer personId) throws DigibigException{
    Map<String, String> map = personMapper.getPersonById(personId);
    //查询地址信息
    Integer node = Integer.valueOf(map.get("addressId"));
    if(node == CommonParam.DEFAULT_INT) {
      map.put("address", "");
    }else {
      map.put("address", addressService.getAddressById(node));
    }
    map.remove("addressId");
    return map;
  }

  
  @Transactional
  @Override
  public Boolean setDefaultMobile(Integer personId, String mobile) {
    
    if(RegexValidator.isMobile(mobile)) {
   
      Mobile aMobile = mobileService.getMobile(personId, mobile);
      
      if(aMobile != null) {
        this.setDefaultMobile(personId,aMobile.getId());
        return true;
      }
      return false;
    }else {
      
      Integer mobileId = Integer.valueOf(mobile);
      Mobile aMobile = mobileMapper.selectByPrimaryKey(mobileId);
      
      if(aMobile != null) {
        this.setDefaultMobile(personId,mobileId);
        return true;
      }
      return false;
    }
  }

  
  @Transactional
  @Override
  public Boolean setDefaultAddress(Integer personId, Integer addressId) {
    
    Address addr = addressMapper.selectByPrimaryKey(addressId);
    if(addr == null) {
      return false;
    }
    
    Person person = new Person();
    person.setId(personId);
    person.setDefaultAddress(addressId);
    person.setLastTime(new Date());
    
    Integer rows = null;
    
    try {
      rows = personMapper.updateByPrimaryKeySelective(person);
    }catch(RuntimeException e) {
      throw new DBException("数据库操作异常",e);
    }
    return rows>0;
  }

  @Override
  public Integer getdefaultAddress(Integer personId) {

    return personMapper.getdefaultAddress(personId);
  }

  @Override
  public Integer getdefaultIDCard(Integer personId) {

    return personMapper.getdefaultIDCard(personId);
  }

  
  @Transactional
  @Override
  public Boolean setDefaultIDCard(Integer personId, Integer idCardId) {
    Person person = new Person();
    person.setId(personId);
    person.setDefaultIdCard(idCardId);
    person.setLastTime(new Date());
    
    Integer rows = null;
    try {
      rows = personMapper.updateByPrimaryKeySelective(person);
    }catch(RuntimeException e) {
      throw new DBException("数据库操作异常",e);
    }
    if(rows == 0) {
      return false;
    }
    return true;
  }

}
