/*
 * Copyright 2017 - 数多科技
 * 
 * 北京数多信息科技有限公司
 * 本公司保留所有下述内容的权利。
 * 本内容为保密信息，仅限本公司内部使用。
 * 非经本公司书面许可，任何人不得外泄或用于其他目的。
 */
package com.digibig.service.person.controller.external;

import com.digibig.service.person.common.CommonParam;
import com.digibig.service.person.domain.Mobile;
import com.digibig.service.person.utils.Enabled;
import com.digibig.spring.api.HttpResult;
import com.digibig.spring.api.HttpStatus;
import com.digibig.spring.credential.Credential;
import com.digibig.spring.credential.CredentialHelper;
import java.util.List;
import java.util.Map;

import java.util.Objects;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.digibig.service.person.service.MobileService;


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

  private com.digibig.service.person.controller.internal.MobileController mobileController;

  @Autowired
  private CredentialHelper credentialHelper;

  @PostMapping("add")
  public HttpResult<Mobile> addMobile(@RequestBody Mobile mobile) {

    return mobileController.addJson(mobile);
  }


  /**
   * 设置手机号状态
   */
//  @PostMapping("/enabled")
//  public HttpResult<Boolean> setMobileEnabled(@RequestBody Map<String, String> paramMap){
//
//    Integer personId = Integer.valueOf(paramMap.get(CommonParam.MAP_PARAM_PERSONID));
//    String mobile = paramMap.get(CommonParam.MAP_PARAM_MOBILE);
//    Enabled enabled = Enum.valueOf(Enabled.class, paramMap.get(CommonParam.MAP_PARAM_ENABLED).trim());
//
//    logger.info("personId:{},mobile:{}",personId,mobile);
//
//    Boolean result = mobileService.setMobileEnabled(personId, mobile, enabled);
//
//    if(result) {
//      return new HttpResult<>(HttpStatus.OK,"成功",result);
//    }
//    return new HttpResult<>(HttpStatus.SERVER_ERROR,"失败",result);
//  }
  @PostMapping("/update")
  public HttpResult<Mobile> setMobileEnabled(@RequestBody Mobile mobile) {

    return mobileController.updateSelectiveJson(mobile);
  }


  @PostMapping("/list/des")
  public HttpResult<List<Mobile>> getDesensitizeInfo(@RequestParam("personId") Integer personId) {

    return new HttpResult<>(HttpStatus.OK, "成功", mobileService.getDesensitizeInfo(personId));
  }


  @PostMapping("/list")
  public HttpResult<List<Mobile>> getMobileInfo(@RequestParam("personId") Integer personId,
      @RequestParam("credentialKey") String credentialKey) {

    //验证授权信息
    Credential credential = credentialHelper.get(credentialKey);
    if (Objects.isNull(credential) || !credential.isSameOrigin()) {
      return new HttpResult<>(HttpStatus.AUTH_FAIL, "授权失败");
    }
    credentialHelper.finish(credential);


    return new HttpResult<>(HttpStatus.OK, "成功", mobileService.listWithParent(personId));
  }
}
