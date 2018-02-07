/*
  * Copyright 2017 - 数多科技
 * 
 * 北京数多信息科技有限公司
 * 本公司保留所有下述内容的权利。
 * 本内容为保密信息，仅限本公司内部使用。
 * 非经本公司书面许可，任何人不得外泄或用于其他目的。
 */
package com.digibig.service.person.controller.external;

import com.digibig.service.person.domain.Education;
import com.digibig.service.person.service.EducationService;
import com.digibig.spring.api.HttpResult;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



@RestController("EducationController-e")
@RequestMapping("/v1.0/person/education")
public class EducationController {
  
  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private EducationService educationService;

  @Autowired
  private com.digibig.service.person.controller.internal.EducationController educationController;

  

  @PostMapping("add")
  public HttpResult<Education> addEducation(@RequestBody Education Education){

    return educationController.addJson(Education);
  }

  @PostMapping("/update")
  public HttpResult<Education> setEducation(@RequestBody Education Education){
    return educationController.updateSelectiveJson(Education);
  }

  @GetMapping("/list")
  public HttpResult<Collection<Education>> getSchoolRecord(@RequestParam("personId") Integer personId){

    return educationController.list(null, null, null, null, personId, null);
  }
}
