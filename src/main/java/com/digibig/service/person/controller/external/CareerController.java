/*
 * Copyright 2017 - 数多科技
 * 
 * 北京数多信息科技有限公司
 * 本公司保留所有下述内容的权利。
 * 本内容为保密信息，仅限本公司内部使用。
 * 非经本公司书面许可，任何人不得外泄或用于其他目的。
 */
package com.digibig.service.person.controller.external;

import com.digibig.service.person.domain.Career;
import com.digibig.service.person.service.CareerService;
import com.digibig.spring.api.HttpResult;
import com.digibig.spring.api.HttpStatus;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController("CareerController-e")
@RequestMapping("/v1.0/person/career")
public class CareerController {
  
  private Logger logger = LoggerFactory.getLogger(getClass());

  private com.digibig.service.person.controller.internal.CareerController careerController;

  @Autowired
  private CareerService careerService;

  @PostMapping("add")
  public HttpResult<Career> addCareer(@RequestBody Career career){

    return new HttpResult<>(HttpStatus.OK,"成功",careerService.add(career));
  }
  

  @PostMapping("/update")
  public HttpResult<Career> setCareer(@RequestBody Career career){
    
    return careerController.updateSelectiveJson(career);
  }

//  @PostMapping("/enabled")
//  public HttpResult<Boolean> setCareerEnable(@RequestBody Map<String, String> paramMap){
//
//    Assert.isTrue(!StringUtils.isEmpty(paramMap.get(CommonParam.MAP_PARAM_PERSONID)), "修改自然人职业经历有效性personId不能为空");
//    Assert.isTrue(!StringUtils.isEmpty(paramMap.get(CAREER_ID)), "修改自然人职业经历有效性careerId不能为空");
//    Assert.isTrue(!StringUtils.isEmpty(paramMap.get(CommonParam.MAP_PARAM_ENABLED)), "修改自然人职业经历有效性enabled不能为空");
//
//    Integer personId = Integer.valueOf(paramMap.get(CommonParam.MAP_PARAM_PERSONID));
//    Integer careerId = Integer.valueOf(paramMap.get(CAREER_ID));
//    Enabled enabled = Enum.valueOf(Enabled.class, paramMap.get(CommonParam.MAP_PARAM_ENABLED).trim());
//
//    Boolean result = careerService.setCareerEnable(personId,careerId,enabled);
//
//    if(result) {
//      return new HttpResult<>(HttpStatus.OK,"成功",result);
//    }
//    return new HttpResult<>(HttpStatus.SERVER_ERROR,"失败",result);
//  }

  /**
   * <p>
   * 查询自然人职业信息
   * </p>
   * @param personId 自然人id
   * @return 自然人职业经历信息
   */
  @GetMapping("/{personId}")
  public HttpResult<List<Career>> getCareers(@PathVariable("personId")Integer personId){

    return new HttpResult<>(HttpStatus.OK,"成功",careerService.listWithParent(personId));
  }
}
