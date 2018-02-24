/*
 * Copyright 2017 - 数多科技
 * 
 * 北京数多信息科技有限公司
 * 本公司保留所有下述内容的权利。
 * 本内容为保密信息，仅限本公司内部使用。
 * 非经本公司书面许可，任何人不得外泄或用于其他目的。
 */
package com.digibig.service.person.controller.external;


import com.digibig.service.person.domain.Person;
import com.digibig.service.person.service.PersonService;
import com.digibig.spring.api.HttpResult;
import com.digibig.spring.api.HttpStatus;
import com.digibig.spring.auth.Domain;
import com.digibig.spring.credential.CredentialHelper;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController("PersonController-e")
@RequestMapping("external/v1.0/person")
@Domain(Person.class)
@Qualifier("external")
public class PersonController {
  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PersonService personService;

  @Autowired
  private CredentialHelper credentialHelper;

  @Autowired
  @Qualifier("internal")
  private com.digibig.service.person.controller.internal.PersonController personController;

  @PostMapping("/add")
  public HttpResult<Person> identityVerificate(@RequestBody Person person){

    return personController.addJson(person);
  }

  /**
   * <p>
   * 按身份证号查询
   * </p>
   */
  @RequestMapping(method = {RequestMethod.POST, RequestMethod.GET},value = "/equal/list")
  public HttpResult<List<Person>> equalListForm(Person data) {
    return personController.equalListJson(data);
  }
  
  /**
   * <p>
   * 设置首选手机号
   * </p>
   */
  @PostMapping("/update")
  public HttpResult<Person> setMobile(@RequestBody Person person){

    return personController.updateSelectiveJson(person);
  }
  
  /**
   * <p>
   * 查询自然人信息 - 脱敏
   * </p>
   */
  @GetMapping("/get/des")
  public HttpResult<Person> getDesensitizeInfo(@RequestParam("personId") Integer personId){

    return new HttpResult<>(HttpStatus.OK,"成功",personService.getDesensitizeInfo(personId));
  }
  

  @GetMapping("/get")
  public HttpResult<Person> getPersonInfo(@RequestParam("personId") Integer personId,
      @RequestParam("credentialKey") String credentialKey){

    credentialHelper.getOnce(credentialKey);

    return personController.get(personId,null,null);
  }
}
