/*
 * Copyright 2017 - 数多科技
 * 
 * 北京数多信息科技有限公司
 * 本公司保留所有下述内容的权利。
 * 本内容为保密信息，仅限本公司内部使用。
 * 非经本公司书面许可，任何人不得外泄或用于其他目的。
 */
package com.digibig.saaserp.person.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.digibig.saaserp.commons.exception.DBException;
import com.digibig.saaserp.person.common.CommonParam;
import com.digibig.saaserp.person.domain.IDCard;
import com.digibig.saaserp.person.domain.IDCardExample;
import com.digibig.saaserp.person.mapper.IDCardMapper;
import com.digibig.saaserp.person.service.IDCardService;
import com.digibig.saaserp.person.service.PersonService;

@Service
public class IDCardServiceImpl implements IDCardService {
  
  private Logger logger = LoggerFactory.getLogger(getClass());
  
  @Autowired
  private IDCardMapper idCardMapper;
  
  @Autowired
  private PersonService personService;

  /**
   * 通过唯一码获取身份证
   * @param uniqueCode 唯一码
   * @return 身份证
   */
  private IDCard getIDCard(String uniqueCode) {
    IDCardExample example = new IDCardExample();
    example.createCriteria().andUniqueCodeEqualTo(uniqueCode);
    
    List<IDCard> idCards = idCardMapper.selectByExample(example);
    if(idCards.size() == 0) {
      return null;
    }
    return idCards.get(0);
  }
  
  /*
   * 绑定身份证
   */
  @Transactional
  @Override
  public Integer addIdCard(IDCard idCard, Boolean isDdefault) {
    
    // 身份证号+发行日期作为唯一码
    String uniqueCode = idCard.getNumber() + idCard.getIssueDate().getTime();
    Integer result = null;

    IDCard aIDCard = this.getIDCard(uniqueCode);
    
    //身份证不存在则添加身份证
    if(aIDCard == null) {
      
      try {
        idCard.setUniqueCode(uniqueCode);
        idCardMapper.insertSelective(idCard);
      }catch(RuntimeException e) {
        logger.error("数据库操作异常",e);
        throw new DBException("数据库操作异常",e);
      }
      result = idCard.getId();
      
    }else{//身份证已存在
      //判断已存在的身份证有无图片，传入的数据有无图片，
//      若原数据无图片而本次传入的信息有，添加图片信息,返回身份证id,否则返回null
      if(aIDCard.getFrontPicture() == CommonParam.DEFAULT_INT 
          && aIDCard.getBackPicture() == CommonParam.DEFAULT_INT 
          && idCard.getFrontPicture() != null && idCard.getBackPicture() != null) {
        idCard.setId(aIDCard.getId());
        
        this.setCardPicture(idCard.getPersonId(), idCard.getId(), idCard.getFrontPicture(), idCard.getBackPicture());
        result = idCard.getId();
        
      }else {
        return null;
      }
    }
    
    //如需设置首选，设置首选 
    if(isDdefault) {
      Boolean re = personService.setDefaultIDCard(idCard.getPersonId(), result);
      if(!re) {
        logger.error("设置默认身份证失败");
      }
    }
    return result;
  }

  
  /*
   * 设置身份证照片
   */
  @Transactional
  @Override
  public Boolean setCardPicture(Integer personId, Integer idCardId, Integer frontPic,
      Integer backPic) {
    IDCardExample example = new IDCardExample();
    example.createCriteria().andIdEqualTo(idCardId).andPersonIdEqualTo(personId);
    
    IDCard idCard = new IDCard();
    if(frontPic == null) {
      idCard.setFrontPicture(CommonParam.DEFAULT_INT);
    }else {
      idCard.setFrontPicture(frontPic);
    }
    if(backPic == null) {
      idCard.setBackPicture(CommonParam.DEFAULT_INT);
    }else {
      idCard.setBackPicture(backPic);
    }
    
    Integer rows = null;
    try {
      rows = idCardMapper.updateByExampleSelective(idCard, example);
    }catch(RuntimeException e) {
      logger.error("数据库操作异常",e);
      throw new DBException("数据库操作异常",e);
    }
    return rows>0;
  }
}
