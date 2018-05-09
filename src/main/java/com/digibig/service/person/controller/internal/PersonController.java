/*
 * Copyright 2017 - 数多科技
 *
 * 北京数多信息科技有限公司 本公司保留所有下述内容的权利。
 * 本内容为保密信息，仅限本公司内部使用。
 * 非经本公司书面许可，任何人不得外泄或用于其他目的。
 */
package com.digibig.service.person.controller.internal;


import com.digibig.service.person.domain.Person;
import com.digibig.service.person.service.PersonService;
import com.digibig.spring.annotation.Code;
import com.digibig.spring.auth.Domain;
import com.digibig.spring.auth.NoLogin;
import com.digibig.spring.basecontroller.AbstractControllerForBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/internal/v1.0/person", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Domain(Person.class)
@Qualifier("internal")
@NoLogin
@Code("person")
public class PersonController extends AbstractControllerForBase<Person> {

  Logger logger = LoggerFactory.getLogger(this.getClass());

  private PersonService service;

  @Autowired
  public PersonController(PersonService service) {
    super(service);
    this.service = service;
    this.enableAdd = false;
  }


//  /**
//   * <p>
//   * 查询自然人信息 - 脱敏
//   * </p>
//   * @param personId 自然人id
//   * @return 自然人信息
//   */
//  @GetMapping("/{id}/des")
//  public HttpResult<Map<String,Object>> getDesensitizeInfo(@PathVariable("id") Integer personId){
//
//    Map<String, Object> result = null;
//    try {
//      result = personService.getDesensitizeInfo(personId);
//    } catch (DigibigException e) {
//      logger.error("查询自然人信息 - 脱敏:",e);
//    }
//
//    return new HttpResult<>(HttpStatus.OK,"成功",result);
//  }
//
}