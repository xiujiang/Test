/*
 * Copyright 2017 - 数多科技
 *
 * 北京数多信息科技有限公司 本公司保留所有下述内容的权利。
 * 本内容为保密信息，仅限本公司内部使用。
 * 非经本公司书面许可，任何人不得外泄或用于其他目的。
 */
package com.digibig.service.person.controller.internal;

import com.digibig.commons.util.RegexValidator;
import com.digibig.service.person.domain.Email;
import com.digibig.service.person.service.EmailService;
import com.digibig.spring.annotation.Code;
import com.digibig.spring.auth.Domain;
import com.digibig.spring.auth.NoLogin;
import com.digibig.spring.basecontroller.AbstractControllerForItem;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/internal/v1.0/email", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@NoLogin
@Code("email")
@Validated
public class EmailController extends AbstractControllerForItem<Email> {

  Logger logger = LoggerFactory.getLogger(this.getClass());

  private EmailService service;

  @Autowired
  public EmailController(EmailService service) {
    super(service);
    this.service = service;
    this.enableList_Parent = true;
  }

  @Override
  protected void preAdd(Email email){
    if(!RegexValidator.isEmail(email.getEmail())){
      throw new IllegalArgumentException("邮箱格式有误。");
    }
  }

  @Override
  protected void preUpdate(Email email){
    if(StringUtils.isNotEmpty(email.getEmail()) && !RegexValidator.isEmail(email.getEmail())){
      throw new IllegalArgumentException("邮箱格式有误。");
    }
  }



//  /**
//   * <p>
//   * 查询自然人邮箱信息 - 脱敏
//   * </p>
//   * @param paramMap
//   * <ul>
//   *    <li>personId 自然人id</li>
//   *    <li>enabled 有效性（可选）</li>
//   * </ul>
//   * @return 邮箱信息
//   */
//  @PostMapping("/list/des")
//  public HttpResult<List<String>> getDesensitizeInfo(@RequestBody Map<String, String> paramMap){
//
//    String personIdStr = paramMap.get(CommonParam.MAP_PARAM_PERSONID);
//    String isEnabled = paramMap.get(CommonParam.MAP_PARAM_ENABLED);
//
//    Assert.isTrue(!StringUtils.isEmpty(personIdStr), "查询自然人邮箱信息 - 脱敏personId不能为空");
//
//    Enabled enabled = Enabled.ENABLED;
//
//    if(!StringUtils.isEmpty(isEnabled)) {
//      enabled = Enum.valueOf(Enabled.class, isEnabled.trim());
//    }
//
//    Integer personId = Integer.valueOf(personIdStr);
//
//    List<String> result = emailService.getDesensitizeInfo(personId, enabled);
//
//    return new HttpResult<>(HttpStatus.OK,"成功",result);
//  }
}