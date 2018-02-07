/*
 * Copyright 2017 - 数多科技
 *
 * 北京数多信息科技有限公司 本公司保留所有下述内容的权利。
 * 本内容为保密信息，仅限本公司内部使用。
 * 非经本公司书面许可，任何人不得外泄或用于其他目的。
 */
package com.digibig.service.person.controller.internal;


import com.digibig.service.person.domain.CareerItem;
import com.digibig.service.person.service.CareerItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.digibig.spring.auth.Domain;
import com.digibig.spring.auth.NoLogin;
import com.digibig.spring.basecontroller.AbstractControllerForItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "internal/v1.0/career/item", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Domain(CareerItem.class)
@Qualifier("internal")
@NoLogin
public class CareerItemController extends AbstractControllerForItem<CareerItem> {

  Logger logger = LoggerFactory.getLogger(this.getClass());

  private CareerItemService service;

  @Autowired
  public CareerItemController(CareerItemService service) {
    super(service);
    this.service = service;
    this.enableList_Parent = true;
  }
}