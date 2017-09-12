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
import com.digibig.saaserp.person.domain.DiplomaCertificate;
import com.digibig.saaserp.person.domain.SchoolRecord;
import com.digibig.saaserp.person.service.DiplomaCertificateService;
import com.digibig.saaserp.person.utils.DegreeGetType;
import com.digibig.saaserp.person.utils.DiplomaType;
import com.digibig.saaserp.person.utils.Enabled;
import com.digibig.saaserp.person.utils.PhaseType;
import com.digibig.saaserp.person.utils.SchoolResult;
import com.digibig.saaserp.person.utils.VarificationStatus;

@RestController
@RequestMapping("/v1.0/person/certificate")
public class DiplomaCertificateController {
  private Logger logger = LoggerFactory.getLogger(getClass());
  
  @Autowired
  private DiplomaCertificateService certificateService;
  
  /**
   * 添加学位证书信息
   * @return
   */
  @PostMapping("/diploma")
  public HttpResult<Integer> addDiplomaCertificate(@RequestBody Map<String, String> paramMap){
    
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get("personId")), "personId不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get("issuer")), "issuer不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get("diploma")), "diploma不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get("certificateNo")), "certificateNo不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get("date")), "date不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get("profession")), "profession不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get("diplomaType")), "diplomaType不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get("type")), "type不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get("variStatus")), "variStatus不能为空");

    DiplomaType diplomaType = Enum.valueOf(DiplomaType.class, paramMap.get("diplomaType").trim());
    DegreeGetType type = Enum.valueOf(DegreeGetType.class, paramMap.get("type").trim());
    VarificationStatus variStatus = Enum.valueOf(VarificationStatus.class, paramMap.get("variStatus").trim());
    
    DiplomaCertificate diploma = new DiplomaCertificate();
    BeanUtilsBean beanUtils = BeanUtilsBean.getInstance();
    ConvertUtils.register(new DateLocaleConverter(), Date.class); 
    
    try {
      beanUtils.populate(diploma,paramMap);
      
    } catch (IllegalAccessException e) {
      
      logger.error(e.getMessage());
      return new HttpResult<Integer>(HttpStatus.PARAM_ERROR,"失败");
      
    } catch (InvocationTargetException e) {
      
      logger.error(e.getMessage());
      return new HttpResult<Integer>(HttpStatus.PARAM_ERROR,"失败");
    }
    
    diploma.setDiplomaType(diplomaType.getValue());
    diploma.setType(type.getValue());
    diploma.setVarificationStatus(variStatus.getValue());

    logger.info(diploma.toString());
    
    Integer id = certificateService.addDiplomaCertificate(diploma);

    if(id == null) {
      return new HttpResult<Integer>(HttpStatus.SERVER_ERROR,"失败，该学位证书已存在");
    }
    return new HttpResult<Integer>(HttpStatus.OK,"成功",id);
  }
  
  /**
   * 修改学位证书信息
   * @return
   */
  @PostMapping("/diploma/mod")
  public HttpResult<Boolean> setDiplomaCertificate(@RequestBody Map<String, String> paramMap){

    Assert.isTrue(!StringUtils.isEmpty(paramMap.get("personId")), "personId不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get("id")), "id不能为空");

    DiplomaType diplomaType = Enum.valueOf(DiplomaType.class, paramMap.get("diplomaType").trim());
    DegreeGetType type = Enum.valueOf(DegreeGetType.class, paramMap.get("type").trim());
    VarificationStatus variStatus = Enum.valueOf(VarificationStatus.class, paramMap.get("variStatus").trim());
    
    DiplomaCertificate diploma = new DiplomaCertificate();
    BeanUtilsBean beanUtils = BeanUtilsBean.getInstance();
    ConvertUtils.register(new DateLocaleConverter(), Date.class); 
    
    try {
      beanUtils.populate(diploma,paramMap);
      
    } catch (IllegalAccessException e) {
      
      logger.error(e.getMessage());
      return new HttpResult<Boolean>(HttpStatus.PARAM_ERROR,"失败");
      
    } catch (InvocationTargetException e) {
      
      logger.error(e.getMessage());
      return new HttpResult<Boolean>(HttpStatus.PARAM_ERROR,"失败");
    }
    
    diploma.setDiplomaType(diplomaType.getValue());
    diploma.setType(type.getValue());
    diploma.setVarificationStatus(variStatus.getValue());

    logger.info(diploma.toString());
    
    Boolean result = certificateService.setDiplomaCertificate(diploma);

    if(result) {
      return new HttpResult<Boolean>(HttpStatus.OK,"成功",result);
    }
    return new HttpResult<Boolean>(HttpStatus.SERVER_ERROR,"失败",result);
  }
  /**
   * 设置学位证书信息的有效性
   * @return
   */
  @PostMapping("/diploma/enabled")
  public HttpResult<Boolean> setCertificateEnabled(@RequestBody Map<String, String> paramMap){
    
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get("personId")), "personId不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get("certificateId")), "certificateId不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get("enabled")), "enabled不能为空");
    
    Integer personId = Integer.valueOf(paramMap.get("personId"));
    Integer certificateId = Integer.valueOf(paramMap.get("certificateId"));
    Enabled enabled = Enum.valueOf(Enabled.class, paramMap.get("enabled").trim());
    
    Boolean result = certificateService.setEnabled(personId,certificateId,enabled);

    if(result) {
      return new HttpResult<Boolean>(HttpStatus.OK,"成功",result);
    }
    return new HttpResult<Boolean>(HttpStatus.SERVER_ERROR,"失败",result);
  }
  
  /**
   * 添加学历证书信息
   * @return
   */
  @PostMapping("/school")
  public HttpResult<Integer> addSchoolRecord(@RequestBody Map<String, String> paramMap){
    
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get("personId")), "personId不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get("certificateNo")), "certificateNo不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get("issuer")), "issuer不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get("profession")), "profession不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get("result")), "result不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get("type")), "type不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get("phase")), "phase不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get("date")), "date不能为空");
    
    SchoolResult result = Enum.valueOf(SchoolResult.class, paramMap.get("result").trim());
    DegreeGetType type = Enum.valueOf(DegreeGetType.class, paramMap.get("type").trim());
    PhaseType phase = Enum.valueOf(PhaseType.class, paramMap.get("phase").trim());

    SchoolRecord record = new SchoolRecord();
    BeanUtilsBean beanUtils = BeanUtilsBean.getInstance();
    ConvertUtils.register(new DateLocaleConverter(), Date.class); 
    
    try {
      beanUtils.populate(record,paramMap);
    } catch (IllegalAccessException e) {
      logger.error(e.getMessage());
      return new HttpResult<Integer>(HttpStatus.PARAM_ERROR,"失败");
    } catch (InvocationTargetException e) {
      logger.error(e.getMessage());
      return new HttpResult<Integer>(HttpStatus.PARAM_ERROR,"失败");
    }
    
    if(!StringUtils.isEmpty(paramMap.get("veriStatus"))) {
      VarificationStatus veriStatus = Enum.valueOf(VarificationStatus.class, paramMap.get("veriStatus").trim());
      record.setVerificationStatus(veriStatus.getValue());
    }
    
    record.setResult(result.getValue());
    record.setType(type.getValue());
    record.setPhase(phase.getValue());

    logger.info(record.toString());
    
    Integer id = certificateService.addSchoolRecord(record);
    
    if(id == null) {
      logger.info("插入失败，证书已存在");
      return new HttpResult<Integer>(HttpStatus.SERVER_ERROR,"失败，该证书已存在");
    }
    return new HttpResult<Integer>(HttpStatus.OK,"成功",id);
  }
  
  /**
   * 修改学历证书信息
   * @return
   */
  @PostMapping("/school/mod")
  public HttpResult<Boolean> setSchoolRecord(@RequestBody Map<String, String> paramMap){
    
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get("personId")), "personId不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get("recordId")), "recordId不能为空");

    SchoolRecord record = new SchoolRecord();
    BeanUtilsBean beanUtils = BeanUtilsBean.getInstance();
    ConvertUtils.register(new DateLocaleConverter(), Date.class); 
    
    try {
      beanUtils.populate(record,paramMap);
    } catch (IllegalAccessException e) {
      logger.error(e.getMessage());
      return new HttpResult<Boolean>(HttpStatus.PARAM_ERROR,"失败");
    } catch (InvocationTargetException e) {
      logger.error(e.getMessage());
      return new HttpResult<Boolean>(HttpStatus.PARAM_ERROR,"失败");
    }
    
    if(!StringUtils.isEmpty(paramMap.get("result"))) {
      SchoolResult result = Enum.valueOf(SchoolResult.class, paramMap.get("result").trim());
      record.setResult(result.getValue());
    }
    if(!StringUtils.isEmpty(paramMap.get("type"))) {
      DegreeGetType type = Enum.valueOf(DegreeGetType.class, paramMap.get("type").trim());
      record.setType(type.getValue());
    }
    if(!StringUtils.isEmpty(paramMap.get("phase"))) {
      PhaseType phase = Enum.valueOf(PhaseType.class, paramMap.get("phase").trim());
      record.setPhase(phase.getValue());
    }
    if(!StringUtils.isEmpty(paramMap.get("veriStatus"))) {
      VarificationStatus veriStatus = Enum.valueOf(VarificationStatus.class, paramMap.get("veriStatus").trim());
      record.setVerificationStatus(veriStatus.getValue());
    }

    logger.info(record.toString());
    
    Boolean re = certificateService.setSchoolRecord(record);

    if(re) {
      return new HttpResult<Boolean>(HttpStatus.OK,"成功",re);
    }
    return new HttpResult<Boolean>(HttpStatus.SERVER_ERROR,"失败",re);
    
  }
  
  /**
   * 设置学历证书信息的有效性
   * @return
   */
  @PostMapping("/school/enabled")
  public HttpResult<Boolean> setRecordEnabled(@RequestBody Map<String, String> paramMap){

    Assert.isTrue(!StringUtils.isEmpty(paramMap.get("personId")), "personId不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get("recordId")), "recordId不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get("enabled")), "enabled不能为空");
    
    Integer personId = Integer.valueOf(paramMap.get("personId"));
    Integer recordId = Integer.valueOf(paramMap.get("recordId"));
    Enabled enabled = Enum.valueOf(Enabled.class, paramMap.get("enabled").trim());
    
    Boolean result = certificateService.setRecordEnabled(personId,recordId,enabled);

    if(result) {
      return new HttpResult<Boolean>(HttpStatus.OK,"成功",result);
    }
    return new HttpResult<Boolean>(HttpStatus.SERVER_ERROR,"失败",result);
  }
  
  /**
   * 添加学信网学历验证报告
   * @return
   */
  @PostMapping("/school/veri")
  public HttpResult<Boolean> addCheckReport(@RequestBody Map<String, String> paramMap){
    
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get("personId")), "personId不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get("recordId")), "recordId不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get("reportFile")), "reportFile不能为空");
    
    Integer personId = Integer.valueOf(paramMap.get("personId"));
    Integer recordId = Integer.valueOf(paramMap.get("recordId"));
    Integer reportFile = Integer.valueOf(paramMap.get("reportFile"));
    
    Boolean result = certificateService.addCheckReport(personId,recordId,reportFile);

    if(result) {
      return new HttpResult<Boolean>(HttpStatus.OK,"成功");
    }else {
      return new HttpResult<Boolean>(HttpStatus.SERVER_ERROR,"失败");
    }
  }
  
  /**
   * 查询自然人学历信息
   * @return
   */
  @GetMapping("/{personId}")
  public HttpResult<List<SchoolRecord>> getSchoolRecord(@PathVariable("personId") Integer personId){
    
    List<SchoolRecord> result = certificateService.getSchoolRecord(personId);

    return new HttpResult<List<SchoolRecord>>(HttpStatus.OK,"成功",result);
  }
  
}
