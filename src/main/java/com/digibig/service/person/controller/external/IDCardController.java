/*
 * Copyright 2017 - 数多科技
 * 
 * 北京数多信息科技有限公司
 * 本公司保留所有下述内容的权利。
 * 本内容为保密信息，仅限本公司内部使用。
 * 非经本公司书面许可，任何人不得外泄或用于其他目的。
 */
package com.digibig.service.person.controller.external;

import com.digibig.service.person.domain.IDCard;
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


/**
 * <p>
 * 身份证相关API，本API提供以下接口：<br>
 * 1、绑定身份证<br>
 * 2、设置身份证关联图片 <br>
 * </p>
 * 
 * @author libin<libin@we.com>
 * @datetime 2017年9月9日下午16:43
 * @version 1.0
 * @since 1.8
 */
@RestController("IDCardController-e")
@RequestMapping("/v1.0/person/idcard")
public class IDCardController {
  
  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private com.digibig.service.person.controller.internal.IDCardController idCardController;

  @PostMapping("add")
  public HttpResult<IDCard> addIdCard(@RequestBody IDCard idCard){
    return idCardController.addJson(idCard);
  }

  //设置身份证关联图片
  @PostMapping("/update")
  public HttpResult<IDCard> setCardPicture(@RequestBody IDCard idCard){
    return idCardController.updateSelectiveJson(idCard);
  }

  @GetMapping("/list")
  public HttpResult<Collection<IDCard>> getIDCard(@RequestParam("personId") Integer personId){
    return idCardController.list(null, null, null, null, personId, null);
  }
}
