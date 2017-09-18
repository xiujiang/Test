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
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.locale.converters.DateLocaleConverter;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.digibig.saaserp.commons.api.HttpResult;
import com.digibig.saaserp.commons.constant.HttpStatus;
import com.digibig.saaserp.person.common.CommonParam;
import com.digibig.saaserp.person.domain.DiplomaCertificate;
import com.digibig.saaserp.person.domain.SchoolRecord;
import com.digibig.saaserp.person.service.DiplomaCertificateService;
import com.digibig.saaserp.person.utils.DegreeGetType;
import com.digibig.saaserp.person.utils.DiplomaType;
import com.digibig.saaserp.person.utils.Enabled;
import com.digibig.saaserp.person.utils.PhaseType;
import com.digibig.saaserp.person.utils.SchoolResult;
import com.digibig.saaserp.person.utils.VerificationStatus;


/**
 * <p>
 * 学历相关API，本API提供以下接口：<br>
 * 1、添加学位证书信息<br>
 * 2、修改学位证书信息 <br>
 * 3、设置学位证书信息的有效性<br>
 * 4、添加学历证书信息<br>
 * 5、修改学历证书信息<br>
 * 6、设置学历证书信息的有效性<br>
 * 7、添加学信网学历验证报告<br>
 * 8、查询自然人学历信息<br>
 * </p>
 * 
 * @author libin<libin@we.com>
 * @datetime 2017年9月9日下午16:43
 * @version 1.0
 * @since 1.8
 */
@RestController
@RequestMapping("/v1.0/person/certificate")
public class DiplomaCertificateController {
  
  private Logger logger = LoggerFactory.getLogger(getClass());

  //颁发证书机构
  private static final String ISSUER = "issuer";
  
  //学位全称
  private static final String DIPLOMA = "diploma";
  
  //证书编号
  private static final String CERTIFICATIE_NUMBER = "certificateNumber";
  
  //日期
  private static final String DATE = "date";
  
  //专业
  private static final String PROFESSION = "profession";
  
  //学位类型
  private static final String DIPLOMA_TYPE = "diplomaType";
  
  //学位学历类型
  private static final String TYPE = "type";
  
  //认证状态
  private static final String VERIFICATION_STATUS = "verificationStatus";
  
  //学位id
  private static final String CERTIFICATE_ID = "certificateId";
  
  //id
  private static final String ID = "id";
  
  //学习成果
  private static final String RESULT = "result";
  
  //学习阶段
  private static final String PHASE = "phase";
  
  //学历id
  private static final String RECORDID = "recordId";
  
  //认证文件id
  private static final String RECORD_FILE = "reportFile";
  
  @Autowired
  private DiplomaCertificateService certificateService;
  
  /**
   * <p>
   * 添加学位证书信息
   * </p>
   * @param paramMap 
   * <ul>
   *    <li>personId 自然人id</li>
   *    <li>issuer 学位授予机构</li>
   *    <li>diploma 学位全名</li>
   *    <li>certificateNumber 证书编号</li>
   *    <li>date 授予日期</li>
   *    <li>profession 所学专业</li>
   *    <li>diplomaType 学位类型</li>
   *    <li>type 学位获得方式</li>
   *    <li>verificationStatus 认证状态</li>
   *    <li>file 学位证影像文件（可选）</li>
   *    <li>verificationFile 学位证认证文件（可选）</li>
   * </ul>
   * @return 学位证id
   */
  @PostMapping("/diploma")
  public HttpResult<Integer> addDiplomaCertificate(@RequestBody Map<String, String> paramMap){
    
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get(CommonParam.MAP_PARAM_PERSONID)), " 添加学位证书personId不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get(ISSUER)), "issuer不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get(DIPLOMA)), "diploma不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get(CERTIFICATIE_NUMBER)), "certificateNumber不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get(DATE)), "date不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get(PROFESSION)), "profession不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get(DIPLOMA_TYPE)), "diplomaType不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get(TYPE)), "type不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get(VERIFICATION_STATUS)), "verificationStatus不能为空");

    DiplomaType diplomaType = Enum.valueOf(DiplomaType.class, paramMap.get(DIPLOMA_TYPE).trim());
    DegreeGetType type = Enum.valueOf(DegreeGetType.class, paramMap.get(TYPE).trim());
    VerificationStatus veriStatus = Enum.valueOf(VerificationStatus.class, paramMap.get(VERIFICATION_STATUS).trim());
    
    DiplomaCertificate diploma = new DiplomaCertificate();
    BeanUtilsBean beanUtils = BeanUtilsBean.getInstance();
    ConvertUtils.register(new DateLocaleConverter(), Date.class); 
    
    try {
      beanUtils.populate(diploma,paramMap);
    } catch (IllegalAccessException | InvocationTargetException e) {
      logger.error("添加学位证书学位证书对象转换异常",e);
      return new HttpResult<>(HttpStatus.PARAM_ERROR,"失败");
    }
    
    diploma.setDiplomaType(diplomaType.getValue());
    diploma.setType(type.getValue());
    diploma.setVerificationStatus(veriStatus.getValue());

    logger.info("",diploma);
    
    Integer id = certificateService.addDiplomaCertificate(diploma);

    if(id == null) {
      return new HttpResult<>(HttpStatus.SERVER_ERROR,"失败，该学位证书已存在");
    }
    return new HttpResult<>(HttpStatus.OK,"成功",id);
  }
  
  /**
   * <p>
   * 修改学位证书信息
   * </p>
   * @param paramMap
   * <ul>
   *    <li>id 学位证书id</li>
   *    <li>personId 自然人id</li>
   *    <li>issuer 学位授予机构（可选）</li>
   *    <li>diploma 学位全名（可选）</li>
   *    <li>certificateNumber 证书编号（可选）</li>
   *    <li>date 授予日期（可选）</li>
   *    <li>profession 所学专业（可选）</li>
   *    <li>diplomaType 学位类型（可选）</li>
   *    <li>type 学位获得方式（可选）</li>
   *    <li>verificationStatus 认证状态（可选）</li>
   *    <li>file 学位证影像文件（可选）</li>
   *    <li>verificationFile 学位证认证文件（可选）</li>
   * </ul>
   * @return 操作结果
   */
  @PostMapping("/diploma/mod")
  public HttpResult<Boolean> setDiplomaCertificate(@RequestBody Map<String, String> paramMap){

    Assert.isTrue(!StringUtils.isEmpty(paramMap.get(CommonParam.MAP_PARAM_PERSONID)), "修改学位证书信息personId不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get(ID)), "id不能为空");

    DiplomaCertificate diploma = new DiplomaCertificate();
    BeanUtilsBean beanUtils = BeanUtilsBean.getInstance();
    ConvertUtils.register(new DateLocaleConverter(), Date.class); 
    
    try {
      beanUtils.populate(diploma,paramMap);
    } catch (IllegalAccessException | InvocationTargetException e) {
      logger.error("修改学位证书学位证书对象转换异常",e);
      return new HttpResult<>(HttpStatus.PARAM_ERROR,"失败");
    }
    
    if(!StringUtils.isEmpty(paramMap.get(DIPLOMA_TYPE))) {
      DiplomaType diplomaType = Enum.valueOf(DiplomaType.class, paramMap.get(DIPLOMA_TYPE).trim());
      diploma.setDiplomaType(diplomaType.getValue());
    }
    if(!StringUtils.isEmpty(paramMap.get(TYPE))) {
      DegreeGetType type = Enum.valueOf(DegreeGetType.class, paramMap.get(TYPE).trim());
      diploma.setType(type.getValue());
    }
    if(!StringUtils.isEmpty(paramMap.get(VERIFICATION_STATUS))) {
      VerificationStatus veriStatus = Enum.valueOf(VerificationStatus.class, paramMap.get(VERIFICATION_STATUS).trim());
      diploma.setVerificationStatus(veriStatus.getValue());
    }
    
    logger.info("",diploma);
    
    Boolean result = certificateService.setDiplomaCertificate(diploma);

    if(result) {
      return new HttpResult<>(HttpStatus.OK,"成功",result);
    }
    return new HttpResult<>(HttpStatus.SERVER_ERROR,"失败",result);
  }
  
  /**
   * <p>
   * 设置学位证书信息的有效性
   * </p>
   * @param paramMap
   * <ul>
   *    <li>personId 自然人id</li>
   *    <li>certificateId 学位证id</li>
   *    <li>enabled 有效性</li>
   * </ul>
   * @return 操作结果
   */
  @PostMapping("/diploma/enabled")
  public HttpResult<Boolean> setCertificateEnabled(@RequestBody Map<String, String> paramMap){
    
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get(CommonParam.MAP_PARAM_PERSONID)), "设置学位证书信息的有效性personId不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get(CERTIFICATE_ID)), "certificateId不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get(CommonParam.MAP_PARAM_ENABLED)), "enabled不能为空");
    
    Integer personId = Integer.valueOf(paramMap.get(CommonParam.MAP_PARAM_PERSONID));
    Integer certificateId = Integer.valueOf(paramMap.get(CERTIFICATE_ID));
    Enabled enabled = Enum.valueOf(Enabled.class, paramMap.get(CommonParam.MAP_PARAM_ENABLED).trim());
    
    Boolean result = certificateService.setEnabled(personId,certificateId,enabled);

    if(result) {
      return new HttpResult<>(HttpStatus.OK,"成功",result);
    }
    return new HttpResult<>(HttpStatus.SERVER_ERROR,"失败",result);
  }
  
  /**
   * <p>
   * 添加学历证书信息
   * </p>
   * @param paramMap
   * <ul>
   *    <li>personId 自然人id</li>
   *    <li>certificateNumber 证书编号</li>
   *    <li>issuer 学历授予机构</li>
   *    <li>profession 所学专业 </li>
   *    <li>result 学习成果</li>
   *    <li>type 学习方式</li>
   *    <li>phase 学习阶段</li>
   *    <li>date 授予日起</li>
   *    <li>file 学历证书文件（可选）</li>
   *    <li>verificationStatus 认证状态（可选）</li>
   *    <li>verificationFile 认证文件（可选）</li>
   * </ul>
   * @return 学历id
   */
  @PostMapping("/school")
  public HttpResult<Integer> addSchoolRecord(@RequestBody Map<String, String> paramMap){
    
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get(CommonParam.MAP_PARAM_PERSONID)), "添加学历证书信息personId不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get(CERTIFICATIE_NUMBER)), "certificateNumber不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get(ISSUER)), "issuer不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get(PROFESSION)), "profession不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get(RESULT)), "result不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get(TYPE)), "type不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get(PHASE)), "phase不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get(DATE)), "date不能为空");
    
    SchoolResult result = Enum.valueOf(SchoolResult.class, paramMap.get(RESULT).trim());
    DegreeGetType type = Enum.valueOf(DegreeGetType.class, paramMap.get(TYPE).trim());
    PhaseType phase = Enum.valueOf(PhaseType.class, paramMap.get(PHASE).trim());

    SchoolRecord record = new SchoolRecord();
    BeanUtilsBean beanUtils = BeanUtilsBean.getInstance();
    ConvertUtils.register(new DateLocaleConverter(), Date.class); 
    
    try {
      beanUtils.populate(record,paramMap);
    } catch (IllegalAccessException | InvocationTargetException e) {
      logger.error("添加学历证书学历证书对象转换异常",e);
      return new HttpResult<>(HttpStatus.PARAM_ERROR,"失败");
    }
    
    if(!StringUtils.isEmpty(paramMap.get(VERIFICATION_STATUS))) {
      VerificationStatus veriStatus = Enum.valueOf(VerificationStatus.class, paramMap.get(VERIFICATION_STATUS).trim());
      record.setVerificationStatus(veriStatus.getValue());
    }
    
    record.setResult(result.getValue());
    record.setType(type.getValue());
    record.setPhase(phase.getValue());

    logger.info("",record);
    
    Integer id = certificateService.addSchoolRecord(record);
    
    if(id == null) {
      logger.info("插入失败，证书已存在");
      return new HttpResult<>(HttpStatus.SERVER_ERROR,"失败，该证书已存在");
    }
    return new HttpResult<>(HttpStatus.OK,"成功",id);
  }
  
  /**
   * <p>
   * 修改学历证书信息
   * </p>
   * @param paramMap
   * <ul>
   *    <li>id 学历证书id</li>
   *    <li>personId 自然人id</li>
   *    <li>certificateNumber 证书编号（可选）</li>
   *    <li>issuer 学历授予机构（可选）</li>
   *    <li>profession 所学专业 （可选）</li>
   *    <li>result 学习成果（可选）</li>
   *    <li>type 学习方式（可选）</li>
   *    <li>phase 学习阶段（可选）</li>
   *    <li>date 授予日期（可选）</li>
   *    <li>file 学历证书文件（可选）</li>
   *    <li>verificationStatus 认证状态（可选）</li>
   *    <li>verificationFile 认证文件（可选）</li>
   * </ul>
   * @return 操作结果
   */
  @PostMapping("/school/mod")
  public HttpResult<Boolean> setSchoolRecord(@RequestBody Map<String, String> paramMap){
    
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get(CommonParam.MAP_PARAM_PERSONID)), "修改学历证书信息personId不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get(ID)), "id不能为空");

    SchoolRecord record = new SchoolRecord();
    BeanUtilsBean beanUtils = BeanUtilsBean.getInstance();
    ConvertUtils.register(new DateLocaleConverter(), Date.class); 
    
    try {
      beanUtils.populate(record,paramMap);
    } catch (IllegalAccessException | InvocationTargetException e) {
      logger.error("修改学历证书学历证书对象转换异常",e);
      return new HttpResult<>(HttpStatus.PARAM_ERROR,"失败");
    }
    
    if(!StringUtils.isEmpty(paramMap.get(RESULT))) {
      SchoolResult result = Enum.valueOf(SchoolResult.class, paramMap.get(RESULT).trim());
      record.setResult(result.getValue());
    }
    if(!StringUtils.isEmpty(paramMap.get(TYPE))) {
      DegreeGetType type = Enum.valueOf(DegreeGetType.class, paramMap.get(TYPE).trim());
      record.setType(type.getValue());
    }
    if(!StringUtils.isEmpty(paramMap.get(PHASE))) {
      PhaseType phase = Enum.valueOf(PhaseType.class, paramMap.get(PHASE).trim());
      record.setPhase(phase.getValue());
    }
    if(!StringUtils.isEmpty(paramMap.get(VERIFICATION_STATUS))) {
      VerificationStatus veriStatus = Enum.valueOf(VerificationStatus.class, paramMap.get(VERIFICATION_STATUS).trim());
      record.setVerificationStatus(veriStatus.getValue());
    }

    logger.info("",record);
    
    Boolean re = certificateService.setSchoolRecord(record);

    if(re) {
      return new HttpResult<>(HttpStatus.OK,"成功",re);
    }
    return new HttpResult<>(HttpStatus.SERVER_ERROR,"失败",re);
    
  }
  
  /**
   * <p>
   * 设置学历证书信息的有效性
   * </p>
   * @param paramMap
   * <ul>
   *    <li>personId 自然人id</li>
   *    <li>recordId 学历信息id</li>
   *    <li>enabled 有效性</li>
   * </ul>
   * @return 操作结果
   */
  @PostMapping("/school/enabled")
  public HttpResult<Boolean> setRecordEnabled(@RequestBody Map<String, String> paramMap){

    Assert.isTrue(!StringUtils.isEmpty(paramMap.get(CommonParam.MAP_PARAM_PERSONID)), "设置学历证书信息的有效性personId不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get(RECORDID)), "recordId不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get(CommonParam.MAP_PARAM_ENABLED)), "enabled不能为空");
    
    Integer personId = Integer.valueOf(paramMap.get(CommonParam.MAP_PARAM_PERSONID));
    Integer recordId = Integer.valueOf(paramMap.get(RECORDID));
    Enabled enabled = Enum.valueOf(Enabled.class, paramMap.get(CommonParam.MAP_PARAM_ENABLED).trim());
    
    Boolean result = certificateService.setRecordEnabled(personId,recordId,enabled);

    if(result) {
      return new HttpResult<>(HttpStatus.OK,"成功",result);
    }
    return new HttpResult<>(HttpStatus.SERVER_ERROR,"失败",result);
  }
  
  /**
   * <p>
   * 添加学信网学历验证报告
   * </p>
   * @param paramMap
   * <ul>
   *    <li>personId 自然人id</li>
   *    <li>recordId 学历id</li>
   *    <li>reportFile 认证文件</li>
   * </ul>
   * @return 操作结果
   */
  @PostMapping("/school/veri")
  public HttpResult<Boolean> addCheckReport(@RequestBody Map<String, String> paramMap){
    
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get(CommonParam.MAP_PARAM_PERSONID)), "添加学信网学历验证报告personId不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get(RECORDID)), "recordId不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get(RECORD_FILE)), "reportFile不能为空");
    
    Integer personId = Integer.valueOf(paramMap.get(CommonParam.MAP_PARAM_PERSONID));
    Integer recordId = Integer.valueOf(paramMap.get(RECORDID));
    Integer reportFile = Integer.valueOf(paramMap.get(RECORD_FILE));
    
    Boolean result = certificateService.addCheckReport(personId,recordId,reportFile);

    if(result) {
      return new HttpResult<>(HttpStatus.OK,"成功");
    }else {
      return new HttpResult<>(HttpStatus.SERVER_ERROR,"失败");
    }
  }
  
  /**
   * <p>
   * 查询自然人学历信息
   * </p>
   * @param personId 自然人id
   * @return 学历信息
   */
  @GetMapping("/{personId}")
  public HttpResult<List<Map<String, Object>>> getSchoolRecord(@PathVariable("personId") Integer personId){
    
    List<Map<String, Object>> result = certificateService.getSchoolRecord(personId);

    return new HttpResult<>(HttpStatus.OK,"成功",result);
  }
  
}
