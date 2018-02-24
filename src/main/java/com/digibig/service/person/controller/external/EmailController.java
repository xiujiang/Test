/*
 * Copyright 2017 - 数多科技
 * 
 * 北京数多信息科技有限公司
 * 本公司保留所有下述内容的权利。
 * 本内容为保密信息，仅限本公司内部使用。
 * 非经本公司书面许可，任何人不得外泄或用于其他目的。
 */
package com.digibig.service.person.controller.external;

import com.digibig.service.person.domain.Email;
import com.digibig.service.person.service.EmailService;
import com.digibig.spring.api.HttpResult;
import com.digibig.spring.api.HttpStatus;
import com.digibig.spring.auth.Domain;
import com.digibig.spring.credential.CredentialHelper;
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


@RestController("EmailController-e")
@RequestMapping("external/v1.0/person/email")
@Domain(Email.class)
@Qualifier("external")
public class EmailController {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private EmailService emailService;

  @Autowired
  @Qualifier("internal")
  private com.digibig.service.person.controller.internal.EmailController emailController;

  @Autowired
  private CredentialHelper credentialHelper;

  @PostMapping("add")
  public HttpResult<Email> addEmail(@RequestBody Email email) {

    return emailController.addJson(email);
  }

  @PostMapping("/update")
  public HttpResult<Email> setEmailEnabled(@RequestBody Email email) {

    return emailController.updateSelectiveForm(email);
  }

  @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
      RequestMethod.DELETE}, value = "/switch")
  public HttpResult<Email> switchStatus(
      @RequestParam("id") Integer id,
      @RequestParam("personId") Integer personId,
      @RequestParam("status") String status
  ) {
    return emailController.switchStatus(id, null, personId, status);
  }


  @PostMapping("/list/des")
  public HttpResult<List<Email>> getDesensitizeInfo(@RequestParam Integer personId) {


    return new HttpResult<>(HttpStatus.OK, "成功", emailService.getDesensitizeInfo(personId));
  }

  @PostMapping("/list")
  public HttpResult<Collection<Email>> getMobileInfo(@RequestParam("personId") Integer personId,
      @RequestParam("credentialKey") String credentialKey) {
    //验证授权信息
    credentialHelper.getOnce(credentialKey);
    return emailController.list(null, null, null, null, personId, null);
  }
}
