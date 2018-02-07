/*
 * Copyright 2017 - 数多科技
 * 
 * 北京数多信息科技有限公司
 * 本公司保留所有下述内容的权利。
 * 本内容为保密信息，仅限本公司内部使用。
 * 非经本公司书面许可，任何人不得外泄或用于其他目的。
 */
package com.digibig.service.person.controller.external;

import com.digibig.service.person.domain.Address;
import com.digibig.spring.api.HttpResult;
import com.digibig.spring.auth.Domain;
import java.util.Collection;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController("AddressController-e")
@RequestMapping("/v1.0/person/address")
@Domain(Address.class)
@Qualifier("external")
public class AddressController {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  @Qualifier("internal")
  private com.digibig.service.person.controller.internal.AddressController controller;

  @PostMapping("add")
  public HttpResult<Address> addAddress(@RequestBody Address address) {

    return controller.addJson(address);
  }

  @PostMapping("/update")
  public HttpResult<Address> setEnabled(@RequestBody Address address) {
    return controller.updateSelectiveJson(address);
  }

  @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
      RequestMethod.DELETE}, value = "/switch")
  public HttpResult<Address> switchStatus(
      @RequestParam(required = false, name = "id") Integer id,
      @RequestParam(required = false, name = "personId") Integer personId,
      @RequestParam("status") String status
  ) {
    return controller.switchStatus(id,null,personId,status);
  }

  @PostMapping("/list")
  public HttpResult<Collection<Address>> getAddress(
      @RequestParam(required = false, name = "id") Integer id,
      @RequestParam(required = false, name = "idList") List<Integer> idList,
      @RequestParam(required = false, name = "personId") Integer personId,
      @RequestParam(required = false, name = "personIdList") List<Integer> personIdList) {

    return controller.list(id, idList, null, null, personId, personIdList);
  }
}
