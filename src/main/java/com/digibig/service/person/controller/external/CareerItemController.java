/*
 * Copyright 2017 - 数多科技
 * 
 * 北京数多信息科技有限公司
 * 本公司保留所有下述内容的权利。
 * 本内容为保密信息，仅限本公司内部使用。
 * 非经本公司书面许可，任何人不得外泄或用于其他目的。
 */
package com.digibig.service.person.controller.external;

import com.digibig.service.person.domain.CareerItem;
import com.digibig.service.person.service.CareerItemService;
import com.digibig.spring.api.HttpResult;
import com.digibig.spring.api.HttpStatus;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController("CareerItemController-e")
@RequestMapping(value = "/v1.0/person/career/item", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class CareerItemController {

  private Logger logger = LoggerFactory.getLogger(getClass());

  private com.digibig.service.person.controller.internal.CareerItemController itemController;

  @Autowired
  private CareerItemService itemService;

  @PostMapping("/add")
  public HttpResult<CareerItem> addCareerItem(@RequestBody CareerItem careerItem){

    return itemController.addJson(careerItem);
  }

  @PostMapping("/update")
  public HttpResult<CareerItem> setCareerItem(@RequestBody CareerItem careerItem){

    return itemController.updateSelectiveJson(careerItem);
  }

//
//  @PostMapping("/item/enabled")
//  public HttpResult<Boolean> setItemEnabled(@RequestBody Map<String, String> paramMap){
//
//    Assert.isTrue(!StringUtils.isEmpty(paramMap.get(CommonParam.MAP_PARAM_PERSONID)), "修改工作详情状态personId不能为空");
//    Assert.isTrue(!StringUtils.isEmpty(paramMap.get(CAREER_ID)), "修改工作详情状态careerId不能为空");
//    Assert.isTrue(!StringUtils.isEmpty(paramMap.get(CAREER_ITEM_ID)), "careerItemId不能为空");
//    Assert.isTrue(!StringUtils.isEmpty(paramMap.get(CommonParam.MAP_PARAM_ENABLED)), "enabled不能为空");
//
//    Integer personId = Integer.valueOf(paramMap.get(CommonParam.MAP_PARAM_PERSONID));
//    Integer careerId = Integer.valueOf(paramMap.get(CAREER_ID));
//    Integer careerItemId = Integer.valueOf(paramMap.get(CAREER_ITEM_ID));
//    Enabled enabled = Enum.valueOf(Enabled.class, paramMap.get(CommonParam.MAP_PARAM_ENABLED).trim());
//
//    Boolean result = careerService.setItemEnabled(personId,careerId,careerItemId,enabled);
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
  @GetMapping("/list")
  public HttpResult<List<CareerItem>> getCareers(@RequestParam("personId")Integer personId){

    return new HttpResult<>(HttpStatus.OK,"成功",itemService.listWithParent(personId));
  }
}
