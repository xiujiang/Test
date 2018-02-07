/*
 * Copyright 2017 - 数多科技
 *
 * 北京数多信息科技有限公司 本公司保留所有下述内容的权利。
 * 本内容为保密信息，仅限本公司内部使用。
 * 非经本公司书面许可，任何人不得外泄或用于其他目的。
 */
package com.digibig.service.person.controller.internal;

import com.digibig.service.person.domain.Address;
import com.digibig.service.person.service.AddressService;
import com.digibig.spring.auth.Domain;
import com.digibig.spring.auth.NoLogin;
import com.digibig.spring.basecontroller.AbstractControllerForItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "internal/v1.0/address", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Domain(Address.class)
@Qualifier("internal")
@NoLogin
public class AddressController extends AbstractControllerForItem<Address> {

  Logger logger = LoggerFactory.getLogger(this.getClass());

  private AddressService service;

  @Autowired
  public AddressController(AddressService service) {
    super(service);
    this.service = service;
    this.enableSwitch_Parent_Id = true;
  }
}