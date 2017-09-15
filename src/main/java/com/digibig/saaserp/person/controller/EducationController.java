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
import com.digibig.saaserp.person.common.CommonParam;
import com.digibig.saaserp.person.domain.Education;
import com.digibig.saaserp.person.domain.EducationSummary;
import com.digibig.saaserp.person.service.EducationService;
import com.digibig.saaserp.person.utils.DegreeGetType;
import com.digibig.saaserp.person.utils.Enabled;
import com.digibig.saaserp.person.utils.PhaseType;
import com.digibig.saaserp.person.utils.VarificationStatus;


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
  
  @Autowired
  private EducationService educationService;
  
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
    
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get("personId")), "personId不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get("startDate")), "startDate不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get("schoolName")), "schoolName不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get("faculty")), "faculty不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get("profession")), "profession不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get("type")), "type不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get("phase")), "phase不能为空");
    
    Education education = new Education();
    BeanUtilsBean beanUtils = BeanUtilsBean.getInstance();
    ConvertUtils.register(new DateLocaleConverter(), Date.class); 
    
    DegreeGetType type = Enum.valueOf(DegreeGetType.class, paramMap.get("type").trim());
    PhaseType phase = Enum.valueOf(PhaseType.class, paramMap.get("phase").trim());
    
    try {
      beanUtils.populate(education,paramMap);
    } catch (IllegalAccessException e) {
      logger.error(e.getMessage());
      return new HttpResult<Integer>(HttpStatus.PARAM_ERROR,"失败");
    } catch (InvocationTargetException e) {
      logger.error(e.getMessage());
      return new HttpResult<Integer>(HttpStatus.PARAM_ERROR,"失败");
    }
    
    if(StringUtils.isEmpty(paramMap.get("end")) || CommonParam.NOW.equals(paramMap.get("end"))) {
      Date endDate = DateUtil.str2Date(CommonParam.NOW_DATE, DateUtil.DATE);
      education.setEndDate(endDate);
    }else {
      Date endDate = DateUtil.str2Date(paramMap.get("end"), DateUtil.DATE);
      if(endDate.after(new Date())) {
        return new HttpResult<Integer>(HttpStatus.PARAM_ERROR,"截止日期有误");
      }
      education.setEndDate(endDate);
    }
    
    education.setType(type.getValue());
    education.setPhase(phase.getValue());
    logger.info(education.toString());
    
    Integer id = educationService.addEducation(education);

    return new HttpResult<Integer>(HttpStatus.OK,"成功",id);
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
    
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get("personId")), "personId不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get("id")), "id不能为空");
    
    String typeStr = paramMap.get("type");
    String phaseStr = paramMap.get("phase");
    
    Education education = new Education();
    BeanUtilsBean beanUtils = BeanUtilsBean.getInstance();
    ConvertUtils.register(new DateLocaleConverter(), Date.class); 
    
    try {
      beanUtils.populate(education,paramMap);
    } catch (IllegalAccessException e) {
      logger.error(e.getMessage());
      return new HttpResult<Boolean>(HttpStatus.PARAM_ERROR,"失败");
    } catch (InvocationTargetException e) {
      logger.error(e.getMessage());
      return new HttpResult<Boolean>(HttpStatus.PARAM_ERROR,"失败");
    }
    
    if(StringUtils.isEmpty(paramMap.get("end")) || CommonParam.NOW.equals(paramMap.get("end"))) {
      Date endDate = DateUtil.str2Date(CommonParam.NOW_DATE, DateUtil.DATE);
      education.setEndDate(endDate);
    }else {
      Date endDate = DateUtil.str2Date(paramMap.get("end"), DateUtil.DATE);
      if(endDate.after(new Date())) {
        return new HttpResult<Boolean>(HttpStatus.PARAM_ERROR,"截止日期有误");
      }
      education.setEndDate(endDate);
    }
    
    if(!StringUtils.isEmpty(typeStr)) {
      DegreeGetType type = Enum.valueOf(DegreeGetType.class, typeStr.trim());
      education.setType(type.getValue());
    }
    if(!StringUtils.isEmpty(phaseStr)) {
      PhaseType phase = Enum.valueOf(PhaseType.class, phaseStr.trim());
      education.setPhase(phase.getValue());
    }
    
    logger.info(education.toString());
    
    Boolean result = educationService.setEducation(education);

    if(result) {
      return new HttpResult<Boolean>(HttpStatus.OK,"成功",result);
    }
    
    return new HttpResult<Boolean>(HttpStatus.SERVER_ERROR,"失败",result);
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
    
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get("personId")), "personId不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get("educationId")), "educationId不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get("enabled")), "enabled不能为空");
    
    Integer personId = Integer.valueOf(paramMap.get("personId"));
    Integer educationId = Integer.valueOf(paramMap.get("educationId"));
    Enabled enabled = Enum.valueOf(Enabled.class, paramMap.get("enabled").trim());

    Boolean result = educationService.setEducationEnabled(personId,educationId,enabled);
    
    if(result) {
      return new HttpResult<Boolean>(HttpStatus.OK,"成功",result);
    }
    
    return new HttpResult<Boolean>(HttpStatus.SERVER_ERROR,"失败",result);
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
    
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get("personId")), "personId不能为空");
    
    EducationSummary educationSummary = new EducationSummary();
    BeanUtilsBean beanUtils = BeanUtilsBean.getInstance();
    
    try {
      beanUtils.populate(educationSummary,paramMap);
    } catch (IllegalAccessException e) {
      logger.error(e.getMessage());
      return new HttpResult<Integer>(HttpStatus.PARAM_ERROR,"失败");
    } catch (InvocationTargetException e) {
      logger.error(e.getMessage());
      return new HttpResult<Integer>(HttpStatus.PARAM_ERROR,"失败");
    }

    if(!StringUtils.isEmpty(paramMap.get("bachelorDegreeType"))) {
      DegreeGetType bDegreeType = Enum.valueOf(DegreeGetType.class, paramMap.get("bachelorDegreeType").trim());
      educationSummary.setBachelorDegreeType(bDegreeType.getValue());
    }
    if(!StringUtils.isEmpty(paramMap.get("bachelorDegreeVerification"))) {
      VarificationStatus bDegreeVerification = Enum.valueOf(VarificationStatus.class,paramMap.get("bachelorDegreeVerification").trim());
      educationSummary.setBachelorDegreeVerification(bDegreeVerification.getValue());
    }
    if(!StringUtils.isEmpty(paramMap.get("masterDegreeType"))) {
      DegreeGetType mDegreeType = Enum.valueOf(DegreeGetType.class, paramMap.get("masterDegreeType").trim());
      educationSummary.setMasterDegreeType(mDegreeType.getValue());
    }
    if(!StringUtils.isEmpty(paramMap.get("masterDegreeVerification"))) {
      VarificationStatus mDegreeVerification = Enum.valueOf(VarificationStatus.class,paramMap.get("masterDegreeVerification").trim());
      educationSummary.setMasterDegreeVerification(mDegreeVerification.getValue());
    }

    logger.info(educationSummary.toString());
    
    Integer id = educationService.setEducationSummary(educationSummary);

    return new HttpResult<Integer>(HttpStatus.OK,"成功",id);
  }
}
