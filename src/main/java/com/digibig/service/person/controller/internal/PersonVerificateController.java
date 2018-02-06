package com.digibig.service.person.controller.internal;

import com.digibig.service.person.domain.Person;
import com.digibig.service.person.service.PersonVerificateService;
import com.digibig.spring.api.HttpResult;
import com.digibig.spring.api.HttpStatus;
import com.digibig.spring.auth.Domain;
import com.digibig.spring.auth.NoLogin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/internal/v1.0/person/verificate", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Domain(Person.class)
@Qualifier("internal")
@NoLogin
public class PersonVerificateController {

  Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  private PersonVerificateService verificateService;

  @PostMapping(value = "",consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  public com.digibig.spring.api.HttpResult<Person> verificate(Person person){

    Assert.isTrue(!StringUtils.isEmpty(person.getName()),"name 不能为空。");
    Assert.isTrue(!StringUtils.isEmpty(person.getIdNumber()),"idNumber 不能为空。");

    return new HttpResult<>(HttpStatus.OK,"成功",verificateService.identityVerificate(person));

  }
}
