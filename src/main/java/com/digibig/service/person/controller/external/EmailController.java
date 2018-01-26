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
import com.digibig.spring.credential.Credential;
import com.digibig.spring.credential.CredentialHelper;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



@RestController("EmailController-e")
@RequestMapping("/v1.0/person/email")
public class EmailController {
  private Logger logger = LoggerFactory.getLogger(getClass());
  
  @Autowired
  private EmailService emailService;

  private com.digibig.service.person.controller.internal.EmailController emailController;

  @Autowired
  private CredentialHelper credentialHelper;

  @PostMapping("add")
  public HttpResult<Email> addEmail(@RequestBody Email email){

    return emailController.addJson(email);
  }
  @PostMapping("/update")
  public HttpResult<Email> setEmailEnabled(@RequestBody Email email){

    return emailController.updateSelectiveForm(email);
  }
//  @PostMapping("/enabled")
//  public HttpResult<Boolean> setEmailEnabled(@RequestBody Map<String, String> paramMap){
//
//    Assert.isTrue(!StringUtils.isEmpty(paramMap.get(CommonParam.MAP_PARAM_PERSONID)), "设置邮箱状态personId不能为空");
//    Assert.isTrue(!StringUtils.isEmpty(paramMap.get(CommonParam.MAP_PARAM_EMAILID)), "设置邮箱状态emailId不能为空");
//    Assert.isTrue(!StringUtils.isEmpty(paramMap.get(CommonParam.MAP_PARAM_ENABLED)), "设置邮箱状态enabled不能为空");
//
//    Integer personId = Integer.valueOf(paramMap.get(CommonParam.MAP_PARAM_PERSONID));
//    Integer emailId = Integer.valueOf(paramMap.get(CommonParam.MAP_PARAM_EMAILID));
//    Enabled enabled = Enum.valueOf(Enabled.class, paramMap.get(CommonParam.MAP_PARAM_ENABLED).trim());
//
//    logger.info("personId:{},emailId:{}",personId,emailId);
//
//    Boolean result = emailService.setEmailEnabled(personId, emailId, enabled);
//
//    if(result) {
//      return new HttpResult<>(HttpStatus.OK,"成功",result);
//    }
//    return new HttpResult<>(HttpStatus.SERVER_ERROR,"失败",result);
//  }
  

  @PostMapping("/list/des")
  public HttpResult<List<Email>> getDesensitizeInfo(@RequestParam Integer personId){

    return new HttpResult<>(HttpStatus.OK,"成功",emailService.getDesensitizeInfo(personId));
  }

  @PostMapping("/list")
  public HttpResult<List<Email>> getMobileInfo(@RequestParam Integer personId,@RequestParam("credentialKey") String credentialKey){
    //验证授权信息
    Credential credential = credentialHelper.get(credentialKey);
    if (Objects.isNull(credential) || !credential.isSameOrigin()) {
      return new HttpResult<>(HttpStatus.AUTH_FAIL, "授权失败");
    }
    credentialHelper.finish(credential);
    return new HttpResult<>(HttpStatus.OK,"成功",emailService.listWithParent(personId));
  }
}
