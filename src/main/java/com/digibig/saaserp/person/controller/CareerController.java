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
import com.digibig.saaserp.commons.util.DateUtil;
import com.digibig.saaserp.person.common.CommonParam;
import com.digibig.saaserp.person.domain.Career;
import com.digibig.saaserp.person.domain.CareerItem;
import com.digibig.saaserp.person.service.CareerService;
import com.digibig.saaserp.person.utils.Current;
import com.digibig.saaserp.person.utils.Enabled;

/**
 * <p>
 * 地址相关API，本API提供以下接口：<br>
 * 1、添加自然人职业经历<br>
 * 2、修改自然人职业经历 <br>
 * 3、修改自然人职业经历有效性<br>
 * 4、添加工作详情<br>
 * 5、修改工作详情 <br>
 * 6、修改工作详情有效性<br>
 * 7、查询自然人职业信息<br>
 * </p>
 * 
 * @author libin<libin@we.com>
 * @datetime 2017年9月9日下午16:43
 * @version 1.0
 * @since 1.8
 */
@RestController
@RequestMapping("/v1.0/person/career")
public class CareerController {
  private Logger logger = LoggerFactory.getLogger(getClass());
  
  @Autowired
  private CareerService careerService;
  
  /**
   * 添加自然人职业经历
   * @param paramMap 参数列表
   *            personId 自然人id
   *            startDate 工作经历开始时间
   *            end 工作经历结束时间
   *            companyFullName 公司全称
   *            companyShortName 公司简称
   * @return 职业经历id
   */
  @PostMapping("")
  public HttpResult<Integer> addCareer(@RequestBody Map<String, String> paramMap){

    Assert.isTrue(!StringUtils.isEmpty(paramMap.get("personId")), "personId不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get("startDate")), "startDate不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get("companyFullName")), "companyFullName不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get("companyShortName")), "companyShortName不能为空");
    
    Career career = new Career();
    BeanUtilsBean beanUtils = BeanUtilsBean.getInstance();
    ConvertUtils.register(new DateLocaleConverter(), Date.class); 
    
    try {
      beanUtils.populate(career,paramMap);
      
    } catch (IllegalAccessException e) {
      
      logger.error(e.getMessage());
      return new HttpResult<Integer>(HttpStatus.PARAM_ERROR,"失败");
      
    } catch (InvocationTargetException e) {
      
      logger.error(e.getMessage());
      return new HttpResult<Integer>(HttpStatus.PARAM_ERROR,"失败");
    }
    
    Current current = null;
    Date endDate = null;
    
    if(StringUtils.isEmpty(paramMap.get("end")) || CommonParam.NOW.equals(paramMap.get("end"))) {
      endDate = DateUtil.str2Date(CommonParam.NOW_DATE, DateUtil.DATE);
      current = Current.IS_CURRENT;
      career.setEndDate(endDate);
      career.setIsCurrent(current.getValue());
    }else {
      endDate = DateUtil.str2Date(paramMap.get("end"), DateUtil.DATE);
      if(endDate.after(new Date())) {
        return new HttpResult<Integer>(HttpStatus.PARAM_ERROR,"截止日期有误");
      }
      current = Current.NOT_CURRENT;
      career.setEndDate(endDate);
      career.setIsCurrent(current.getValue());
    }
    logger.info(career.toString());
    
    Integer id = careerService.addCareer(career);

    if(id == null) {
      return new HttpResult<Integer>(HttpStatus.SERVER_ERROR,"失败,该经历已存在");
    }
    
    return new HttpResult<Integer>(HttpStatus.OK,"成功",id);
  }
  
  /**
   * 修改自然人职业经历
   * @param paramMap 参数列表
   *            personId 自然人id
   *            careerId 工作经历id
   *            startDate 工作经历开始时间
   *            end 工作经历结束时间
   *            companyFullName 公司全称
   *            companyShortName 公司简称
   * @return 操作结果
   */
  @PostMapping("/mod")
  public HttpResult<Boolean> setCareer(@RequestBody Map<String, String> paramMap){
    
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get("personId")), "personId不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get("id")), "careerId不能为空");

    //将参数列表转化为bean
    Career career = new Career();
    BeanUtilsBean beanUtils = BeanUtilsBean.getInstance();
    ConvertUtils.register(new DateLocaleConverter(), Date.class); 
    
    try {
      beanUtils.populate(career,paramMap);
      
    } catch (IllegalAccessException e) {
      
      logger.error(e.getMessage());
      return new HttpResult<Boolean>(HttpStatus.PARAM_ERROR,"失败");
      
    } catch (InvocationTargetException e) {
      
      logger.error(e.getMessage());
      return new HttpResult<Boolean>(HttpStatus.PARAM_ERROR,"失败");
    }
    
    Current current = null;
    Date endDate = null;
    
    if(StringUtils.isEmpty(paramMap.get("end")) || CommonParam.NOW.equals(paramMap.get("end"))) {
      endDate = DateUtil.str2Date(CommonParam.NOW_DATE, DateUtil.DATE);
      current = Current.IS_CURRENT;
      career.setEndDate(endDate);
      career.setIsCurrent(current.getValue());
    }else {
      endDate = DateUtil.str2Date(paramMap.get("end"), DateUtil.DATE);
      if(endDate.after(new Date())) {
        return new HttpResult<Boolean>(HttpStatus.PARAM_ERROR,"截止日期有误");
      }
      current = Current.NOT_CURRENT;
      career.setEndDate(endDate);
      career.setIsCurrent(current.getValue());
    }
    logger.info(career.toString());

    Boolean result = careerService.setCareer(career);

    if(result) {
      return new HttpResult<Boolean>(HttpStatus.OK,"成功",result);
    }
    return new HttpResult<Boolean>(HttpStatus.SERVER_ERROR,"失败",result);
  }
  
  /**
   * 修改自然人职业经历有效性
   *            personId 自然人id
   *            careerId 工作经历id
   *            enabled 工作经历有效性
   * @param paramMap 参数列表
   * @return 操作结果
   */
  @PostMapping("/enabled")
  public HttpResult<Boolean> setCareerEnable(@RequestBody Map<String, String> paramMap){
    
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get("personId")), "personId不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get("careerId")), "careerId不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get("enabled")), "enabled不能为空");
    
    Integer personId = Integer.valueOf(paramMap.get("personId"));
    Integer careerId = Integer.valueOf(paramMap.get("careerId"));
    Enabled enabled = Enum.valueOf(Enabled.class, paramMap.get("enabled").trim());

    Boolean result = careerService.setCareerEnable(personId,careerId,enabled);

    if(result) {
      return new HttpResult<Boolean>(HttpStatus.OK,"成功",result);
    }
    return new HttpResult<Boolean>(HttpStatus.SERVER_ERROR,"失败",result);
  }
  
  /**
   * 添加工作详情
   * @param paramMap 参数列表
   *            personId 自然人id
   *            careerId 工作经历id
   *            department 部门
   *            position 职位
   *            startDate 开始时间
   *            end 结束时间
   *            description 描述
   * @return 工作详情id
   */
  @PostMapping("/item")
  public HttpResult<Integer> addCareerItem(@RequestBody Map<String, String> paramMap){
    
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get("personId")), "personId不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get("careerId")), "careerId不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get("department")), "department不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get("position")), "position不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get("startDate")), "startDate不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get("description")), "description不能为空");
   
    //将参数列表转化为bean
    CareerItem careerItem = new CareerItem();
    BeanUtilsBean beanUtils = BeanUtilsBean.getInstance();
    ConvertUtils.register(new DateLocaleConverter(), Date.class); 
    
    try {
      beanUtils.populate(careerItem,paramMap);
      
    } catch (IllegalAccessException e) {
      
      logger.error(e.getMessage());
      return new HttpResult<Integer>(HttpStatus.PARAM_ERROR,"失败");
      
    } catch (InvocationTargetException e) {
      
      logger.error(e.getMessage());
      return new HttpResult<Integer>(HttpStatus.PARAM_ERROR,"失败");
    }
    
    Current current = null;
    Date endDate = null;
    
    if(StringUtils.isEmpty(paramMap.get("end")) || CommonParam.NOW.equals(paramMap.get("end"))) {
      endDate = DateUtil.str2Date(CommonParam.NOW_DATE, DateUtil.DATE);
      current = Current.IS_CURRENT;
      careerItem.setEndDate(endDate);
      careerItem.setIsCurrent(current.getValue());
    }else {
      endDate = DateUtil.str2Date(paramMap.get("end"), DateUtil.DATE);
      if(endDate.after(new Date())) {
        return new HttpResult<Integer>(HttpStatus.PARAM_ERROR,"截止日期有误");
      }
      current = Current.NOT_CURRENT;
      careerItem.setEndDate(endDate);
      careerItem.setIsCurrent(current.getValue());
    }
    logger.info(careerItem.toString());
    
    Integer id = careerService.addCareerItem(careerItem);

    if(id != null) {
      return new HttpResult<Integer>(HttpStatus.OK,"成功",id);
    }else {
      return new HttpResult<Integer>(HttpStatus.SERVER_ERROR,"无对应的工作经历");
    }
    
  }
  
  /**
   * 修改工作详情
   * @param paramMap 参数列表
   *            personId 自然人id
   *            careerId 工作经历id
   *            careerItemId 工作详情id
   *            department 部门
   *            position 职位
   *            startDate 开始时间
   *            end 结束时间
   *            description 描述
   * @return 操作结果
   */
  @PostMapping("/item/mod")
  public HttpResult<Boolean> setCareerItem(@RequestBody Map<String, String> paramMap){
    
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get("personId")), "personId不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get("careerId")), "careerId不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get("careerItemId")), "careerItemId不能为空");
 
    //将参数列表转化为bean
    CareerItem careerItem = new CareerItem();
    BeanUtilsBean beanUtils = BeanUtilsBean.getInstance();
    ConvertUtils.register(new DateLocaleConverter(), Date.class); 
    
    try {
      beanUtils.populate(careerItem,paramMap);
      
    } catch (IllegalAccessException e) {
      
      logger.error(e.getMessage());
      return new HttpResult<Boolean>(HttpStatus.PARAM_ERROR,e.getMessage());
      
    } catch (InvocationTargetException e) {
      
      logger.error(e.getMessage());
      return new HttpResult<Boolean>(HttpStatus.PARAM_ERROR,e.getMessage());
    }
    
    Current current = null;
    Date endDate = null;
    
    if(StringUtils.isEmpty(paramMap.get("end")) || CommonParam.NOW.equals(paramMap.get("end"))) {
      endDate = DateUtil.str2Date(CommonParam.NOW_DATE, DateUtil.DATE);
      current = Current.IS_CURRENT;
      careerItem.setEndDate(endDate);
      careerItem.setIsCurrent(current.getValue());
    }else {
      endDate = DateUtil.str2Date(paramMap.get("end"), DateUtil.DATE);
      if(endDate.after(new Date())) {
        return new HttpResult<Boolean>(HttpStatus.PARAM_ERROR,"截止日期有误");
      }
      current = Current.NOT_CURRENT;
      careerItem.setEndDate(endDate);
      careerItem.setIsCurrent(current.getValue());
    }
    logger.info(careerItem.toString());
    
    Boolean result = careerService.setCareerItem(careerItem);

    
    if(result) {
      return new HttpResult<Boolean>(HttpStatus.OK,"成功",result);
    }
    return new HttpResult<Boolean>(HttpStatus.SERVER_ERROR,"失败",result);
  }
  
  /**
   * 修改工作详情状态
   * @param paramMap 参数列表
   *            personId 自然人id
   *            careerId 工作经历id
   *            careerItemId 工作详情id
   *            enabled 工作详情有效性
   * @return 操作结果
   */
  @PostMapping("/item/enabled")
  public HttpResult<Boolean> setItemEnabled(@RequestBody Map<String, String> paramMap){

    Assert.isTrue(!StringUtils.isEmpty(paramMap.get("personId")), "personId不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get("careerId")), "careerId不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get("careerItemId")), "careerItemId不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get("enabled")), "enabled不能为空");
    
    Integer personId = Integer.valueOf(paramMap.get("personId"));
    Integer careerId = Integer.valueOf(paramMap.get("careerId"));
    Integer careerItemId = Integer.valueOf(paramMap.get("careerItemId"));
    Enabled enabled = Enum.valueOf(Enabled.class, paramMap.get("enabled").trim());

    Boolean result = careerService.setItemEnabled(personId,careerId,careerItemId,enabled);

    if(result) {
      return new HttpResult<Boolean>(HttpStatus.OK,"成功",result);
    }
    return new HttpResult<Boolean>(HttpStatus.SERVER_ERROR,"失败",result);
  }
  
  /**
   * 查询自然人职业信息
   * @param personId 自然人id
   * @return 自然人职业经历信息
   */
  @GetMapping("/{personId}")
  public HttpResult<List<Map<String ,Object>>> getCareers(@PathVariable("personId")Integer personId){
    List<Map<String ,Object>> list = careerService.getCareers(personId);
    return new HttpResult<List<Map<String ,Object>>>(HttpStatus.OK,"成功",list);
  }
}
