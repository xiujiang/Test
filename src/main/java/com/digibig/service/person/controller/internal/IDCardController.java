/*
 * Copyright 2017 - 数多科技
 *
 * 北京数多信息科技有限公司 本公司保留所有下述内容的权利。
 * 本内容为保密信息，仅限本公司内部使用。
 * 非经本公司书面许可，任何人不得外泄或用于其他目的。
 */
package com.digibig.service.person.controller.internal;


import com.digibig.commons.util.IDValidator;
import com.digibig.service.person.domain.IDCard;
import com.digibig.service.person.service.IDCardService;
import com.digibig.spring.annotation.Code;
import com.digibig.spring.auth.Domain;
import com.digibig.spring.auth.NoLogin;
import com.digibig.spring.basecontroller.AbstractControllerForItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "internal/v1.0/idcard", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Domain(IDCard.class)
@Qualifier("internal")
@NoLogin
@Code("idCard")
public class IDCardController extends AbstractControllerForItem<IDCard> {

  Logger logger = LoggerFactory.getLogger(this.getClass());

  private IDCardService service;

  @Autowired
  public IDCardController(IDCardService service) {
    super(service);
    this.service = service;
    this.enableList_Parent = true;
  }

  @Override
  protected void preAdd(IDCard idCard){
    if(!IDValidator.valid(idCard.getNumber())){
      throw new IllegalArgumentException("身份证号有误。");
    }
  }

  @Override
  protected void preUpdate(IDCard idCard){
    if(!StringUtils.isEmpty(idCard.getNumber()) && !IDValidator.valid(idCard.getNumber())){
      throw new IllegalArgumentException("身份证号有误。");
    }
  }
}