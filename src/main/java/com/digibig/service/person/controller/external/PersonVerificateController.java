package com.digibig.service.person.controller.external;

import com.digibig.service.person.domain.Person;
import com.digibig.service.person.service.PersonVerificateService;
import com.digibig.spring.api.HttpStatus;
import com.digibig.spring.auth.Domain;
import com.digibig.spring.auth.NoLogin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("PersonVerificateController-e")
@RequestMapping(value = "external/v1.0/person/verificate", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class PersonVerificateController {

  Logger logger = LoggerFactory.getLogger(this.getClass());


  private com.digibig.service.person.controller.internal.PersonVerificateController verificateController;

  @PostMapping(value = "")
  public com.digibig.spring.api.HttpResult<Person> verificate(@RequestBody Person person){
    return verificateController.verificate(person);
  }
}
