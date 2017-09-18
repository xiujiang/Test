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
import org.springframework.util.CollectionUtils;

import com.digibig.saaserp.commons.exception.DBException;
import com.digibig.saaserp.commons.util.MaskedUtil;
import com.digibig.saaserp.person.common.CommonParam;
import com.digibig.saaserp.person.domain.Email;
import com.digibig.saaserp.person.domain.EmailExample;
import com.digibig.saaserp.person.mapper.EmailMapper;
import com.digibig.saaserp.person.service.EmailService;
import com.digibig.saaserp.person.service.PersonService;
import com.digibig.saaserp.person.utils.Enabled;

@Service
public class EmailServiceImpl implements EmailService {
  
  private Logger logger = LoggerFactory.getLogger(getClass());
  
  @Autowired
  private EmailMapper emailMapper;
  
  @Autowired
  private PersonService personService;

  /*
   * 添加邮箱
   */
  @Transactional
  @Override
  @Caching(evict = {
      @CacheEvict(value = "email",
          key = "'com.digibig.saaserp.person.domain.email_des_person_id_'+#personId"),
      @CacheEvict(value = "email",
          key = "'com.digibig.saaserp.person.domain.email_person_id_'+#personId")
      })
  public Integer addEmail(Integer personId, String email, Boolean isDefault) {
    //检查自然人名下邮箱号是否存在
    Email aEmail = getEmail(personId,email);
    Integer result = null;
    //不存在添加邮箱
    if(aEmail == null) {
      result = addEmail(personId,email);
    }else {
      
      if(aEmail.getEnabled() == Enabled.NOT_ENABLED.getValue()) {
        setEmailEnabled(aEmail.getPersonId(), aEmail.getId(), Enabled.ENABLED);
        result = aEmail.getId();
      }else {
        return null;
      }
    }
    //如需设置首选，设置首选
    if(isDefault) {
      Boolean re = personService.setDefaultEmail(personId, result,true);
      if(!re) {
        logger.error("设置默认地址失败");
        return null;
      }
    }
    return result;
  }

  /*
   * 设置邮箱有效性
   */
  @Transactional
  @Override
  @Caching(evict = {
      @CacheEvict(value = "email",
          key = "'com.digibig.saaserp.person.domain.email_des_person_id_'+#personId"),
      @CacheEvict(value = "email",
          key = "'com.digibig.saaserp.person.domain.email_person_id_'+#personId")
      })
  public Boolean setEmailEnabled(Integer personId, Integer emailId, Enabled enabled) {
    
    EmailExample example = new EmailExample();
    example.createCriteria().andIdEqualTo(emailId).andPersonIdEqualTo(personId);
    
    Email email = new Email();
    email.setEnabled(enabled.getValue());
    
    Integer rows = null;
    try {
      rows = emailMapper.updateByExampleSelective(email, example);
    }catch(RuntimeException e) {
      logger.error("setEmailEnabled数据库操作异常",e);
      throw new DBException("setEmailEnabled数据库操作异常" , e);
    }
    
    if(Enabled.NOT_ENABLED.getValue() == enabled.getValue()) {
      personService.delDefaultEmail(personId, emailId);
    }
    
    return rows>0;
  }

  /**
   * 查询邮箱列表 - 脱敏
   */
  @Override
  @Cacheable(value = "email", key = "'com.digibig.saaserp.person.domain.email_des_person_id_'+#personId")
  public List<String> getDesensitizeInfo(Integer personId, Enabled enabled) {
    //查询数据
    List<String> emails = emailMapper.getEmails(personId, enabled.getValue());
    //脱敏处理
    int suf = 0;
    int index = 0;
    for(String email : emails) {
      //对list重新设置或者可以返回新的list
      suf = email.length() - email.lastIndexOf('@');
      email = MaskedUtil.masked(email, CommonParam.EMIAL_DES_PRE, suf);
      emails.set(index, email);
      index++;
    }
    return emails;
  }

  /**
   * 查询邮箱列表
   */
  @Override
  @Cacheable(value = "email", key = "'com.digibig.saaserp.person.domain.email_person_id_'+#personId")
  public List<String> getEmailInfo(Integer personId, Enabled enabled) {
    return emailMapper.getEmails(personId, enabled.getValue());
  }

  
  /**
   * 通过自然人id和邮箱信息查询邮箱
   * @param personId 自然人id
   * @param email 邮箱信息
   * @return 邮箱
   */
  private Email getEmail(Integer personId,String email) {
    EmailExample example = new EmailExample();
    example.createCriteria().andPersonIdEqualTo(personId).andEmailEqualTo(email);
    List<Email> emails = emailMapper.selectByExample(example);
    
    if(CollectionUtils.isEmpty(emails)) {
      return null;
    }
    return emails.get(0);
  }
  
  /**
   * 添加邮箱
   * @param personId 自然人id
   * @param email 邮箱
   * @return 邮箱id
   */
  private Integer addEmail(Integer personId, String email) {
    
    Email aEmail = new Email();
    aEmail.setPersonId(personId);
    aEmail.setEmail(email);
    aEmail.setCreateTime(new Date());
    aEmail.setLastTime(new Date());
    
    try {
      emailMapper.insertSelective(aEmail);
    }catch(RuntimeException e) {
      logger.error("addEmail数据库操作异常",e);
      throw new DBException("addEmail数据库操作异常",e);
    }
    
    return aEmail.getId();
  }
}
