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
import com.digibig.saaserp.commons.exception.DigibigException;
import com.digibig.saaserp.commons.util.DateUtil;
import com.digibig.saaserp.person.common.CommonParam;
import com.digibig.saaserp.person.domain.Education;
import com.digibig.saaserp.person.domain.EducationSummary;
import com.digibig.saaserp.person.service.EducationService;
import com.digibig.saaserp.person.utils.DegreeGetType;
import com.digibig.saaserp.person.utils.Enabled;
import com.digibig.saaserp.person.utils.PhaseType;
import com.digibig.saaserp.person.utils.VerificationStatus;


/**
 * <p>
 * 教育相关API，本API提供以下接口：<br>
 * 1、添加教育经历<br>
 * 2、修改教育经历 <br>
 * 3、设置教育经历的有效性<br>
 * 4、修改教育经历摘要信息<br>
 * </p>
 * 
 * @author libin<libin@we.com>
 * @datetime 2017年9月9日下午16:43
 * @version 1.0
 * @since 1.8
 */
@RestController
@RequestMapping("/v1.0/person/education")
public class EducationController {
  
  private Logger logger = LoggerFactory.getLogger(getClass());
  
  //id
  private static final String ID = "id";
  
  //开始时间
  private static final String START_DATE = "startDate";
  
  //结束时间
  private static final String END_DATE = "end";
  
  //学校名
  private static final String SCHOOL_NAME = "schoolName";
  
  //二级学院
  private static final String FACULTY = "faculty";
  
  //专业
  private static final String PROFESSION = "profession";
  
  //教育id
  private static final String EDUCATIONID = "educationId";
  
  //学习类型
  private static final String TYPE = "type";
  
  //学习阶段
  private static final String PHASE = "phase";
  
  //学士学位获取类型
  private static final String BACHELOR_DEGREE_TYPE = "bachelorDegreeType";
  
  //学士学位认证状态
  private static final String BACHELOR_DEGREE_VERI = "bachelorDegreeVerification";
  
  //硕士学位获取类型
  private static final String MASTER_DEGREE_TYPE = "masterDegreeType";
  
  //硕士学位认证状态
  private static final String MASTER_DEGREE_VERI = "masterDegreeVerification";
  
  @Autowired
  private EducationService educationService;
  
  private Education mapToEducation(Map<String, String> paramMap) throws DigibigException {
    
    Education education = new Education();
    BeanUtilsBean beanUtils = BeanUtilsBean.getInstance();
    ConvertUtils.register(new DateLocaleConverter(), Date.class); 
    
    try {
      beanUtils.populate(education,paramMap);
    } catch (IllegalAccessException | InvocationTargetException e) {
      logger.error("教育经历对象转换异常",e);
      throw new DigibigException("教育经历对象转换异常",e);
    } 
    
    if(StringUtils.isEmpty(paramMap.get(END_DATE)) || CommonParam.NOW.equals(paramMap.get(END_DATE))) {
      Date endDate = DateUtil.str2Date(CommonParam.NOW_DATE, DateUtil.DATE);
      education.setEndDate(endDate);
    }else {
      Date endDate = DateUtil.str2Date(paramMap.get(END_DATE), DateUtil.DATE);
      if(new Date().before(endDate)) {
        throw new DigibigException("截止日期有误");
      }
      education.setEndDate(endDate);
    }
    
    if(!StringUtils.isEmpty(paramMap.get(TYPE))) {
      DegreeGetType type = Enum.valueOf(DegreeGetType.class, paramMap.get(TYPE).trim());
      education.setType(type.getValue());
    }
    if(!StringUtils.isEmpty(paramMap.get(PHASE))) {
      PhaseType phase = Enum.valueOf(PhaseType.class, paramMap.get(PHASE).trim());
      education.setPhase(phase.getValue());
    }
    
    logger.info("",education);
    
    return education;
  }
  
  /**
   * <p>
   * 添加教育经历
   * </p>
   * @param paramMap
   * <ul>
   *    <li>personId 自然人ID</li>
   *    <li>schoolName 学校名称</li>
   *    <li>startDate 开始日期</li>
   *    <li>end 结束日期（可选）</li>
   *    <li>faculty 二级学院</li>
   *    <li>profession 所学专业名称</li>
   *    <li>type 学习类型</li>
   *    <li>phase 学习阶段</li>
   *    <li>diplomaId 学位证书id（可选）</li>
   *    <li>recordId 学历证书id（可选）</li>
   * </ul>
   * @return 教育经历id
   */
  @PostMapping("")
  public HttpResult<Integer> addEducation(@RequestBody Map<String, String> paramMap){
    
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get(CommonParam.MAP_PARAM_PERSONID)), "添加教育经历personId不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get(START_DATE)), "startDate不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get(SCHOOL_NAME)), "schoolName不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get(FACULTY)), "faculty不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get(PROFESSION)), "profession不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get(TYPE)), "type不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get(PHASE)), "phase不能为空");
    
    Education education;
    try {
      education = mapToEducation(paramMap);
    } catch (DigibigException e) {
      logger.error("添加教育经历教育经历对象转换异常",e);
      return new HttpResult<>(HttpStatus.PARAM_ERROR,e.getMessage());
    }
    
    Integer id = educationService.addEducation(education);

    return new HttpResult<>(HttpStatus.OK,"成功",id);
  }
  
  
  /**
   * <p>
   * 修改教育经历
   * </p>
   * @param paramMap
   * <ul>
   *    <li>id 教育经历id</li>
   *    <li>personId 自然人ID</li>
   *    <li>schoolName 学校名称（可选）</li>
   *    <li>startDate 开始日期（可选）</li>
   *    <li>end 结束日期（可选）</li>
   *    <li>faculty 二级学院（可选）</li>
   *    <li>profession 所学专业名称（可选）</li>
   *    <li>type 学习类型（可选）</li>
   *    <li>phase 学习阶段（可选）</li>
   *    <li>diplomaId 学位证书id（可选）</li>
   *    <li>recordId 学历证书id（可选）</li>
   * </ul>
   * @return 操作结果
   */
  @PostMapping("/mod")
  public HttpResult<Boolean> setEducation(@RequestBody Map<String, String> paramMap){
    
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get(CommonParam.MAP_PARAM_PERSONID)), "修改教育经历personId不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get(ID)), "id不能为空");
    
    Education education;
    try {
      education = mapToEducation(paramMap);
    } catch (DigibigException e) {
      logger.error("修改教育经历教育经历对象转换异常",e);
      return new HttpResult<>(HttpStatus.PARAM_ERROR,e.getMessage());
    }
    
    Boolean result = educationService.setEducation(education);

    if(result) {
      return new HttpResult<>(HttpStatus.OK,"成功",result);
    }
    
    return new HttpResult<>(HttpStatus.SERVER_ERROR,"失败",result);
  }
  
  
  /**
   * <p>
   * 设置教育经历的有效性
   * </p>
   * @param paramMap
   * <ul>
   *    <li>personId 自然人id</li>
   *    <li>educationId 教育经历id</li>
   *    <li>enabled 有效性</li>
   * </ul>
   * @return 操作结果
   */
  @PostMapping("/enabled")
  public HttpResult<Boolean> setEducationEnabled(@RequestBody Map<String, String> paramMap){
    
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get(CommonParam.MAP_PARAM_PERSONID)), "设置教育经历的有效性personId不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get(EDUCATIONID)), "educationId不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get(CommonParam.MAP_PARAM_ENABLED)), "enabled不能为空");
    
    Integer personId = Integer.valueOf(paramMap.get(CommonParam.MAP_PARAM_PERSONID));
    Integer educationId = Integer.valueOf(paramMap.get(EDUCATIONID));
    Enabled enabled = Enum.valueOf(Enabled.class, paramMap.get(CommonParam.MAP_PARAM_ENABLED).trim());

    Boolean result = educationService.setEducationEnabled(personId,educationId,enabled);
    
    if(result) {
      return new HttpResult<>(HttpStatus.OK,"成功",result);
    }
    
    return new HttpResult<>(HttpStatus.SERVER_ERROR,"失败",result);
  }
  
  
  /**
   * <p>
   * 修改教育经历摘要信息
   * </p>
   * @param paramMap
   * <ul>
   *    <li>personId 自然人ID</li>
   *    <li>lastDegree 最高学位（可选）</li>
   *    <li>bachelorDegreeIssuer 学士学位授予机构（可选）</li>
   *    <li>bachelorDegreeProfession 学士学位专业（可选）</li>
   *    <li>bachelorDegreeType 学士学位取得方式（可选）</li>
   *    <li>bachelorDegreeYear 学士学位授予年份（可选）</li>
   *    <li>bachelorDegreeRefId 学士学位关联记录号（可选）</li>
   *    <li>bachelorDegreeVerification 学士学位认证状态（可选）</li>
   *    <li>masterDegreeIssuer 硕士学位授予机构（可选）</li>
   *    <li>masterDegreeProfession 硕士学位专业（可选）</li>
   *    <li>masterDegreeType 硕士学位取得方式（可选）</li>
   *    <li>masterDegreeYear 硕士学位授予年份（可选）</li>
   *    <li>masterDegreeRefId 硕士学位关联记录号（可选）</li>
   *    <li>masterDegreeVerification 硕士学位认证状态（可选）</li>
   * </ul>
   * @return 摘要id
   */
  @PostMapping("/summary")
  public HttpResult<Integer> setEducationSummary(@RequestBody Map<String, String> paramMap){
    
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get(CommonParam.MAP_PARAM_PERSONID)), "personId不能为空");
    
    EducationSummary educationSummary = new EducationSummary();
    BeanUtilsBean beanUtils = BeanUtilsBean.getInstance();
    
    try {
      beanUtils.populate(educationSummary,paramMap);
    } catch (IllegalAccessException | InvocationTargetException e) {
      logger.error("教育摘要对象转换异常",e);
      return new HttpResult<>(HttpStatus.PARAM_ERROR,"失败");
    } 

    if(!StringUtils.isEmpty(paramMap.get(BACHELOR_DEGREE_TYPE))) {
      DegreeGetType bDegreeType = Enum.valueOf(DegreeGetType.class, paramMap.get(BACHELOR_DEGREE_TYPE).trim());
      educationSummary.setBachelorDegreeType(bDegreeType.getValue());
    }
    if(!StringUtils.isEmpty(paramMap.get(BACHELOR_DEGREE_VERI))) {
      VerificationStatus bDegreeVerification = Enum.valueOf(VerificationStatus.class,paramMap.get(BACHELOR_DEGREE_VERI).trim());
      educationSummary.setBachelorDegreeVerification(bDegreeVerification.getValue());
    }
    if(!StringUtils.isEmpty(paramMap.get(MASTER_DEGREE_TYPE))) {
      DegreeGetType mDegreeType = Enum.valueOf(DegreeGetType.class, paramMap.get(MASTER_DEGREE_TYPE).trim());
      educationSummary.setMasterDegreeType(mDegreeType.getValue());
    }
    if(!StringUtils.isEmpty(paramMap.get(MASTER_DEGREE_VERI))) {
      VerificationStatus mDegreeVerification = Enum.valueOf(VerificationStatus.class,paramMap.get(MASTER_DEGREE_VERI).trim());
      educationSummary.setMasterDegreeVerification(mDegreeVerification.getValue());
    }

    logger.info("",educationSummary);
    
    Integer id = educationService.setEducationSummary(educationSummary);

    return new HttpResult<>(HttpStatus.OK,"成功",id);
  }
}
