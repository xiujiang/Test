/*
 * Copyright 2017 - 数多科技
 * 
 * 北京数多信息科技有限公司
 * 本公司保留所有下述内容的权利。
 * 本内容为保密信息，仅限本公司内部使用。
 * 非经本公司书面许可，任何人不得外泄或用于其他目的。
 */
package com.digibig.saaserp.person.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.locale.converters.DateLocaleConverter;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.digibig.saaserp.commons.api.HttpResult;
import com.digibig.saaserp.commons.constant.HttpStatus;
import com.digibig.saaserp.commons.util.DateUtil;
import com.digibig.saaserp.commons.util.IDValidator;
import com.digibig.saaserp.person.common.CommonParam;
import com.digibig.saaserp.person.domain.IDCard;
import com.digibig.saaserp.person.service.IDCardService;
import com.digibig.saaserp.person.utils.Gender;
import com.digibig.saaserp.person.utils.IDCardType;


/**
 * <p>
 * 身份证相关API，本API提供以下接口：<br>
 * 1、绑定身份证<br>
 * 2、设置身份证关联图片 <br>
 * </p>
 * 
 * @author libin<libin@we.com>
 * @datetime 2017年9月9日下午16:43
 * @version 1.0
 * @since 1.8
 */
@RestController
@RequestMapping("/v1.0/person/idcard")
public class IDCardController {
  private Logger logger = LoggerFactory.getLogger(getClass());
  
  @Autowired
  private IDCardService idCardService;
  
  /**
   * <p>
   * 绑定身份证
   * </p>
   * @param paramMap
   * <ul>
   *    <li>personId 自然人id</li>
   *    <li>name 姓名</li>
   *    <li>number 身份证号</li>
   *    <li>address 地址</li>
   *    <li>issueDate 发证日期</li>
   *    <li>expire 有效期</li>
   *    <li>pGender 性别</li>
   *    <li>type 身份证类型（可选）</li>
   *    <li>agency 机构</li>
   *    <li>frontPicture 正面照片（可选）</li>
   *    <li>backPicture 背面照片（可选）</li>
   *    <li>isDefault 是否默认（可选）</li>
   * </ul>
   * @return 身份证id
   */
  @PostMapping("")
  public HttpResult<Integer> addIdCard(@RequestBody Map<String, String> paramMap){
    
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get("personId")), "personId不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get("name")), "name不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get("number")), "number不能为空");
    Assert.isTrue(IDValidator.valid(paramMap.get("number")), "number不合法");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get("issueDate")), "issueDate不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get("expire")), "expire不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get("address")), "address不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get("pGender")), "pGender不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get("agency")), "agency不能为空");
    
    IDCard idCard = new IDCard();
    BeanUtilsBean beanUtils = BeanUtilsBean.getInstance();
    ConvertUtils.register(new DateLocaleConverter(), Date.class); 
    
    try {
      beanUtils.populate(idCard,paramMap);
    } catch (IllegalAccessException e) {
      logger.error(e.getMessage());
      return new HttpResult<Integer>(HttpStatus.PARAM_ERROR,"失败");
    } catch (InvocationTargetException e) {
      logger.error(e.getMessage());
      return new HttpResult<Integer>(HttpStatus.PARAM_ERROR,"失败");
    }
    
    String idCardTypeStr = paramMap.get("idCardType");
    String expire = paramMap.get("expire");
    String genderStr = paramMap.get("pGender");
    
    Boolean isDefault = Boolean.valueOf(paramMap.get("isDefault"));

    IDCardType idCaedType = IDCardType.SECOND;
    
    Gender gender = Enum.valueOf(Gender.class, genderStr.trim());
    Date expireDate = null;
    
    if(CommonParam.LONG_TIME.equals(expire)) {
      expireDate = DateUtil.str2Date(CommonParam.NOW_DATE, DateUtil.DATE);
    }else {
      expireDate = DateUtil.str2Date(expire, DateUtil.DATE);
    }
    
    if(!StringUtils.isEmpty(idCardTypeStr)) {
        idCaedType = Enum.valueOf(IDCardType.class, idCardTypeStr.trim());
    }
    
    idCard.setGender(gender.getValue());
    idCard.setExpireDate(expireDate);
    idCard.setType(idCaedType.getValue());
    
    logger.info(idCard.toString());
    
    Integer id = idCardService.addIdCard(idCard,isDefault);

    if(id == null) {
      return new HttpResult<Integer>(HttpStatus.SERVER_ERROR,"该身份证已存在");
    }
    
    return new HttpResult<Integer>(HttpStatus.OK,"成功",id);
  }
  
  /**
   * <p>
   * 设置身份证关联图片
   * </p>
   * @param paramMap
   * <ul>
   *    <li>personId 自然人id</li>
   *    <li>idCardId 身份证id</li>
   *    <li>frontPic 正面图片（可选）</li>
   *    <li>backPic 背面图片（可选）</li>
   * </ul>
   * @return 操作结果
   */
  @PostMapping("/pic")
  public HttpResult<Boolean> setCardPicture(@RequestBody Map<String, String> paramMap){
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get("personId")), "personId不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get("idCardId")), "idCardId不能为空");
    
    String frontPicStr = paramMap.get("frontPic");
    String backPicStr = paramMap.get("backPic");
    
    Integer personId = Integer.valueOf(paramMap.get("personId"));
    Integer idCardId = Integer.valueOf(paramMap.get("idCardId"));
    Integer frontPic = null;
    Integer backPic = null;

    if(!StringUtils.isEmpty(frontPicStr)) {
      frontPic = Integer.valueOf(frontPicStr);
    }
    
    if(!StringUtils.isEmpty(backPicStr)) {
      backPic = Integer.valueOf(backPicStr);
    }
    
    Boolean result = idCardService.setCardPicture(personId,idCardId,frontPic,backPic);

    if(result) {
      return new HttpResult<Boolean>(HttpStatus.OK,"成功",result);
    }
    return new HttpResult<Boolean>(HttpStatus.SERVER_ERROR,"失败",result);
  }
}
