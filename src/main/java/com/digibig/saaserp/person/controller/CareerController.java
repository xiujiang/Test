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
import com.digibig.saaserp.commons.exception.DigibigException;
import com.digibig.saaserp.commons.util.DateUtil;
import com.digibig.saaserp.person.common.CommonParam;
import com.digibig.saaserp.person.domain.Career;
import com.digibig.saaserp.person.domain.CareerItem;
import com.digibig.saaserp.person.service.CareerService;
import com.digibig.saaserp.person.utils.Current;
import com.digibig.saaserp.person.utils.Enabled;

/**
 * <p>
 * 职业相关API，本API提供以下接口：<br>
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
  
  //开始时间
  private static final String START_DATE = "startDate";

  //结束时间
  private static final String END = "end";
  
  //id
  private static final String ID = "id";
  
  //经历id
  private static final String CAREER_ID = "careerId";
  
  //部门
  private static final String DEPARTMENT = "department";
  
  //职位
  private static final String POSITION = "position";
  
  //描述
  private static final String DESCRIPTION = "description";
  
  //详情id
  private static final String CAREER_ITEM_ID = "careerItemId";
  
  //公司全名
  private static final String COMPANY_FULL_NAME = "companyFullName";
  
  //公司简称
  private static final String COMPANY_SHORT_NAME = "companyShortName";
  
  @Autowired
  private CareerService careerService;
  
  /**
   * 将map转化为career对象bean
   * @param paramMap map参数
   * @return career对象
   * @throws DigibigException
   */
  private Career mapToCareer(Map<String, String> paramMap) throws DigibigException {
    Career career = new Career();
    BeanUtilsBean beanUtils = BeanUtilsBean.getInstance();
    ConvertUtils.register(new DateLocaleConverter(), Date.class); 
    
    try {
      beanUtils.populate(career,paramMap);
    } catch (IllegalAccessException | InvocationTargetException e) {
      logger.error("工作经历对象转换异常",e);
      throw new DigibigException("工作经历对象转换异常",e);
    }
    
    Current current = null;
    Date endDate = null;
    
    if(StringUtils.isEmpty(paramMap.get(END)) || CommonParam.NOW.equals(paramMap.get(END))) {
      endDate = DateUtil.str2Date(CommonParam.NOW_DATE, DateUtil.DATE);
      current = Current.IS_CURRENT;
      career.setEndDate(endDate);
      career.setIsCurrent(current.getValue());
    }else {
      endDate = DateUtil.str2Date(paramMap.get(END), DateUtil.DATE);
      if(new Date().before(endDate)) {
        throw new DigibigException("截止日期有误");
      }
      current = Current.NOT_CURRENT;
      career.setEndDate(endDate);
      career.setIsCurrent(current.getValue());
    }
    logger.info("",career);
    
    return career;
  }
  
  /**
   * 将map转化为career对象bean
   * @param paramMap map参数
   * @return career对象
   * @throws DigibigException
   */
  private CareerItem mapToCareerItem(Map<String, String> paramMap) throws DigibigException {
    CareerItem careerItem = new CareerItem();
    BeanUtilsBean beanUtils = BeanUtilsBean.getInstance();
    ConvertUtils.register(new DateLocaleConverter(), Date.class); 
    
    try {
      beanUtils.populate(careerItem,paramMap);
    } catch (IllegalAccessException | InvocationTargetException e) {
      logger.error("经历详情对象转换异常",e);
      throw new DigibigException("经历详情对象转换异常",e);
    }
    
    Current current = null;
    Date endDate = null;
    
    if(StringUtils.isEmpty(paramMap.get(END)) || CommonParam.NOW.equals(paramMap.get(END))) {
      endDate = DateUtil.str2Date(CommonParam.NOW_DATE, DateUtil.DATE);
      current = Current.IS_CURRENT;
      careerItem.setEndDate(endDate);
      careerItem.setIsCurrent(current.getValue());
    }else {
      endDate = DateUtil.str2Date(paramMap.get(END), DateUtil.DATE);
      if(new Date().before(endDate)) {
        throw new DigibigException("截止日期有误");
      }
      current = Current.NOT_CURRENT;
      careerItem.setEndDate(endDate);
      careerItem.setIsCurrent(current.getValue());
    }
    logger.info("",careerItem);
    return careerItem;
  }
  
  /**
   * <p>
   * 添加自然人职业经历
   * </p>
   * @param paramMap 参数列表
   * <ul>
   *     <li>personId 自然人id</li>
   *     <li>startDate 工作经历开始时间</li>
   *     <li>end 工作经历结束时间，可以为“至今”（可选，为空时表示至今）</li>
   *     <li>companyFullName 公司全称</li>
   *     <li>companyShortName 公司简称</li>
   *</ul>
   * @return 职业经历id
   */
  @PostMapping("")
  public HttpResult<Integer> addCareer(@RequestBody Map<String, String> paramMap){

    Assert.isTrue(!StringUtils.isEmpty(paramMap.get(CommonParam.MAP_PARAM_PERSONID)), "添加自然人职业经历personId不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get(START_DATE)), "startDate不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get(COMPANY_FULL_NAME)), "companyFullName不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get(COMPANY_SHORT_NAME)), "companyShortName不能为空");
    
    Career career;
    try {
      career = mapToCareer(paramMap);
    } catch (DigibigException e) {
      return new HttpResult<>(HttpStatus.PARAM_ERROR,e.getMessage());
    }
    
    Integer id = careerService.addCareer(career);

    if(id == null) {
      return new HttpResult<>(HttpStatus.SERVER_ERROR,"失败,该经历已存在");
    }
    
    return new HttpResult<>(HttpStatus.OK,"成功",id);
  }
  
  /**
   * <p>
   * 修改自然人职业经历
   * </p>
   * @param paramMap 参数列表
   * <ul>
   *     <li>personId 自然人id</li>
   *     <li>id 工作经历id</li>
   *     <li>startDate 工作经历开始时间（可选）</li>
   *     <li>end 工作经历结束时间（可选）</li>
   *     <li>companyFullName 公司全称（可选）</li>
   *     <li>companyShortName 公司简称（可选）</li>
   * </ul>
   * @return 操作结果
   */
  @PostMapping("/mod")
  public HttpResult<Boolean> setCareer(@RequestBody Map<String, String> paramMap){
    
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get(CommonParam.MAP_PARAM_PERSONID)), "修改自然人职业经历personId不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get(ID)), "id不能为空");

    //将参数列表转化为bean
    Career career;
    try {
      career = mapToCareer(paramMap);
    } catch (DigibigException e) {
      return new HttpResult<>(HttpStatus.PARAM_ERROR,e.getMessage());
    }

    Boolean result = careerService.setCareer(career);

    if(result) {
      return new HttpResult<>(HttpStatus.OK,"成功",result);
    }
    return new HttpResult<>(HttpStatus.SERVER_ERROR,"失败",result);
  }
  
  /**
   * <p>
   * 修改自然人职业经历有效性
   * </p>
   * @param paramMap 参数列表
   * <ul>
   *     <li>personId 自然人id</li>
   *     <li>careerId 工作经历id</li>
   *     <li>enabled 工作经历有效性</li>
   * </ul>
   * @return 操作结果
   */
  @PostMapping("/enabled")
  public HttpResult<Boolean> setCareerEnable(@RequestBody Map<String, String> paramMap){
    
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get(CommonParam.MAP_PARAM_PERSONID)), "修改自然人职业经历有效性personId不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get(CAREER_ID)), "修改自然人职业经历有效性careerId不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get(CommonParam.MAP_PARAM_ENABLED)), "修改自然人职业经历有效性enabled不能为空");
    
    Integer personId = Integer.valueOf(paramMap.get(CommonParam.MAP_PARAM_PERSONID));
    Integer careerId = Integer.valueOf(paramMap.get(CAREER_ID));
    Enabled enabled = Enum.valueOf(Enabled.class, paramMap.get(CommonParam.MAP_PARAM_ENABLED).trim());

    Boolean result = careerService.setCareerEnable(personId,careerId,enabled);

    if(result) {
      return new HttpResult<>(HttpStatus.OK,"成功",result);
    }
    return new HttpResult<>(HttpStatus.SERVER_ERROR,"失败",result);
  }
  
  /**
   * <p>
   * 添加工作详情
   * </p>
   * @param paramMap 参数列表
   * <ul>
   *     <li>personId 自然人id</li>
   *     <li>careerId 工作经历id</li>
   *     <li>department 部门</li>
   *     <li>position 职位</li>
   *     <li>startDate 开始时间</li>
   *     <li>end 结束时间，可以为“至今”（可选，为空时表示至今）</li>
   *     <li>description 描述</li>
   * </ul>
   * @return 工作详情id
   */
  @PostMapping("/item")
  public HttpResult<Integer> addCareerItem(@RequestBody Map<String, String> paramMap){
    
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get(CommonParam.MAP_PARAM_PERSONID)), "添加工作详情personId不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get(CAREER_ID)), "添加工作详情careerId不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get(DEPARTMENT)), "department不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get(POSITION)), "position不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get(START_DATE)), "startDate不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get(DESCRIPTION)), "description不能为空");
   
    //将参数列表转化为bean
    CareerItem careerItem ;
    try {
      careerItem = mapToCareerItem(paramMap);
    } catch (DigibigException e) {
      return new HttpResult<>(HttpStatus.PARAM_ERROR,e.getMessage());
    }
    
    Integer id = careerService.addCareerItem(careerItem);

    if(id != null) {
      return new HttpResult<>(HttpStatus.OK,"成功",id);
    }else {
      return new HttpResult<>(HttpStatus.SERVER_ERROR,"无有效的工作经历");
    }
    
  }
  
  /**
   * <p>
   * 修改工作详情
   * </p>
   * @param paramMap 参数列表
   * <ul>
   *     <li>personId 自然人id</li>
   *     <li>careerId 工作经历id</li>
   *     <li>id 工作详情id</li>
   *     <li>department 部门（可选）</li>
   *     <li>position 职位（可选）</li>
   *     <li>startDate 开始时间（可选）</li>
   *     <li>end 结束时间，可以为“至今”（可选，为空时表示至今）</li>
   *     <li>description 描述（可选）</li>
   * </ul>
   * @return 操作结果
   */
  @PostMapping("/item/mod")
  public HttpResult<Boolean> setCareerItem(@RequestBody Map<String, String> paramMap){
    
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get(CommonParam.MAP_PARAM_PERSONID)), "修改工作详情personId不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get(CAREER_ID)), "修改工作详情careerId不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get(ID)), "id不能为空");
 
    //将参数列表转化为bean
    CareerItem careerItem ;
    try {
      careerItem = mapToCareerItem(paramMap);
    } catch (DigibigException e) {
      return new HttpResult<>(HttpStatus.PARAM_ERROR,e.getMessage());
    }
    
    Boolean result = careerService.setCareerItem(careerItem);

    if(result) {
      return new HttpResult<>(HttpStatus.OK,"成功",result);
    }
    return new HttpResult<>(HttpStatus.SERVER_ERROR,"失败",result);
  }
  
  /**
   * <p>
   * 修改工作详情状态
   * </p>
   * @param paramMap 参数列表
   * <ul>
   *     <li>personId 自然人id</li>
   *     <li>careerId 工作经历id</li>
   *     <li>careerItemId 工作详情id</li>
   *     <li>enabled 工作详情有效性</li>
   * </ul>
   * @return 操作结果
   */
  @PostMapping("/item/enabled")
  public HttpResult<Boolean> setItemEnabled(@RequestBody Map<String, String> paramMap){

    Assert.isTrue(!StringUtils.isEmpty(paramMap.get(CommonParam.MAP_PARAM_PERSONID)), "修改工作详情状态personId不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get(CAREER_ID)), "修改工作详情状态careerId不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get(CAREER_ITEM_ID)), "careerItemId不能为空");
    Assert.isTrue(!StringUtils.isEmpty(paramMap.get(CommonParam.MAP_PARAM_ENABLED)), "enabled不能为空");
    
    Integer personId = Integer.valueOf(paramMap.get(CommonParam.MAP_PARAM_PERSONID));
    Integer careerId = Integer.valueOf(paramMap.get(CAREER_ID));
    Integer careerItemId = Integer.valueOf(paramMap.get(CAREER_ITEM_ID));
    Enabled enabled = Enum.valueOf(Enabled.class, paramMap.get(CommonParam.MAP_PARAM_ENABLED).trim());

    Boolean result = careerService.setItemEnabled(personId,careerId,careerItemId,enabled);

    if(result) {
      return new HttpResult<>(HttpStatus.OK,"成功",result);
    }
    return new HttpResult<>(HttpStatus.SERVER_ERROR,"失败",result);
  }
  
  /**
   * <p>
   * 查询自然人职业信息
   * </p>
   * @param personId 自然人id
   * @return 自然人职业经历信息
   */
  @GetMapping("/{personId}")
  public HttpResult<List<Map<String ,Object>>> getCareers(@PathVariable("personId")Integer personId){
    List<Map<String ,Object>> list = careerService.getCareers(personId);
    return new HttpResult<>(HttpStatus.OK,"成功",list);
  }
}
