/*
 * Copyright 2017 - 数多科技
 * 
 * 北京数多信息科技有限公司
 * 本公司保留所有下述内容的权利。
 * 本内容为保密信息，仅限本公司内部使用。
 * 非经本公司书面许可，任何人不得外泄或用于其他目的。
 */
package com.digibig.service.person.controller.external;

import com.digibig.service.person.domain.Mobile;
import com.digibig.service.person.service.MobileService;
import com.digibig.spring.api.HttpResult;
import com.digibig.spring.api.HttpStatus;
import com.digibig.spring.credential.Credential;
import com.digibig.spring.credential.CredentialHelper;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * <p> 手机相关API，本API提供以下接口：<br> 1、添加手机号<br> 2、设置手机号有效性 <br> 3、查询自然人手机号信息 - 脱敏<br> 4、查询自然人手机号信息 -
 * 不脱敏<br> </p>
 *
 * @author libin<libin@we.com>
 * @version 1.0
 * @datetime 2017年9月9日下午16:43
 * @since 1.8
 */
@RestController("MobileController-e")
@RequestMapping("/v1.0/person/mobiles")
public class MobileController {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private MobileService mobileService;

  @Autowired
  private com.digibig.service.person.controller.internal.MobileController mobileController;

  @Autowired
  private CredentialHelper credentialHelper;

  @PostMapping("add")
  public HttpResult<Mobile> addMobile(@RequestBody Mobile mobile) {

    return mobileController.addJson(mobile);
  }

  @PostMapping("/update")
  public HttpResult<Mobile> setMobileEnabled(@RequestBody Mobile mobile) {

    return mobileController.updateSelectiveJson(mobile);
  }


  @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
      RequestMethod.DELETE}, value = "/switch")
  public HttpResult<Mobile> switchStatus(
      @RequestParam(required = false, name = "id") Integer id,
      @RequestParam(required = false, name = "personId") Integer personId,
      @RequestParam("status") String status
  ) {
    return mobileController.switchStatus(id, null, personId, status);
  }


  @PostMapping("/list/des")
  public HttpResult<List<Mobile>> getDesensitizeInfo(@RequestParam("personId") Integer personId) {

    return new HttpResult<>(HttpStatus.OK, "成功", mobileService.getDesensitizeInfo(personId));
  }


  @PostMapping("/list")
  public HttpResult<Collection<Mobile>> getMobileInfo(@RequestParam("personId") Integer personId,
      @RequestParam("credentialKey") String credentialKey) {

    //验证授权信息
    Credential credential = credentialHelper.get(credentialKey);
    if (Objects.isNull(credential) || !credential.isSameOrigin()) {
      return new HttpResult<>(HttpStatus.AUTH_FAIL, "授权失败");
    }
    credentialHelper.finish(credential);

    return mobileController.list(null, null, null, null, personId, null);
  }
}
