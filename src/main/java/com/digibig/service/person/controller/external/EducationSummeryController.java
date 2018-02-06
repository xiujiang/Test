/*
  * Copyright 2017 - 数多科技
 * 
 * 北京数多信息科技有限公司
 * 本公司保留所有下述内容的权利。
 * 本内容为保密信息，仅限本公司内部使用。
 * 非经本公司书面许可，任何人不得外泄或用于其他目的。
 */
package com.digibig.service.person.controller.external;

import com.digibig.service.person.domain.EducationSummary;
import com.digibig.service.person.service.EducationSummaryService;
import com.digibig.spring.api.HttpResult;
import com.digibig.spring.api.HttpStatus;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController("EducationSummeryController-e")
@RequestMapping("/v1.0/person/education/summery")
public class EducationSummeryController {
  
  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private EducationSummaryService educationSummaryService;

  @Autowired
  private com.digibig.service.person.controller.internal.EducationSummaryController educationSummaryController;

  @PostMapping("add")
  public HttpResult<EducationSummary> addEducation(@RequestBody EducationSummary educationSummary){

    return educationSummaryController.addJson(educationSummary);
  }


  @PostMapping("/update")
  public HttpResult<EducationSummary> setEducation(@RequestBody EducationSummary educationSummary){

    return educationSummaryController.updateSelectiveJson(educationSummary);
  }


  /**
   * 设置教育经历的有效性
   */
//  @PostMapping("/enabled")
//  public HttpResult<Boolean> setEducationEnabled(@RequestBody Map<String, String> paramMap){
//
//    Assert.isTrue(!StringUtils.isEmpty(paramMap.get(CommonParam.MAP_PARAM_PERSONID)), "设置教育经历的有效性personId不能为空");
//    Assert.isTrue(!StringUtils.isEmpty(paramMap.get(EDUCATIONID)), "educationId不能为空");
//    Assert.isTrue(!StringUtils.isEmpty(paramMap.get(CommonParam.MAP_PARAM_ENABLED)), "enabled不能为空");
//
//    Integer personId = Integer.valueOf(paramMap.get(CommonParam.MAP_PARAM_PERSONID));
//    Integer educationId = Integer.valueOf(paramMap.get(EDUCATIONID));
//    Enabled enabled = Enum.valueOf(Enabled.class, paramMap.get(CommonParam.MAP_PARAM_ENABLED).trim());
//
//    Boolean result = educationService.setEducationEnabled(personId,educationId,enabled);
//
//    if(result) {
//      return new HttpResult<>(HttpStatus.OK,"成功",result);
//    }
//
//    return new HttpResult<>(HttpStatus.SERVER_ERROR,"失败",result);
//  }

  @GetMapping("/list")
  public HttpResult<List<EducationSummary>> getSchoolRecord(@RequestParam("personId") Integer personId){

    return new HttpResult<>(HttpStatus.OK,"成功",educationSummaryService.listWithParent(personId));
  }
}
