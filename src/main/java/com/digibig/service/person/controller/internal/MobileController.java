/*
 * Copyright 2017 - 数多科技
 *
 * 北京数多信息科技有限公司 本公司保留所有下述内容的权利。
 * 本内容为保密信息，仅限本公司内部使用。
 * 非经本公司书面许可，任何人不得外泄或用于其他目的。
 */
package com.digibig.service.person.controller.internal;


import com.digibig.service.person.domain.Mobile;
import com.digibig.service.person.service.MobileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.digibig.spring.auth.Domain;
import com.digibig.spring.auth.NoLogin;
import com.digibig.spring.basecontroller.AbstractControllerForItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "internal/v1.0/mobile", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Domain(Mobile.class)
@Qualifier("internal")
@NoLogin
public class MobileController extends AbstractControllerForItem<Mobile> {

  Logger logger = LoggerFactory.getLogger(this.getClass());

  private MobileService service;

  @Autowired
  public MobileController(MobileService service) {
    super(service);
    this.service = service;
  }

//  /**
//   * <p>
//   * 查询自然人手机号信息 - 脱敏
//   * </p>
//   * @param paramMap
//   * <ul>
//   *    <li>personId 自然人id</li>
//   *    <li>enabled 有效性（可选）</li>
//   * </ul>
//   * @return 手机号信息
//   */
//  @PostMapping("/list/des")
//  public HttpResult<List<String>> getDesensitizeInfo(@RequestBody Map<String, String> paramMap){
//    String personIdStr = paramMap.get(CommonParam.MAP_PARAM_PERSONID);
//    String isEnabled = paramMap.get(CommonParam.MAP_PARAM_ENABLED);
//
//    Assert.isTrue(!StringUtils.isEmpty(personIdStr), "查询自然人手机号信息 - 脱敏personId不能为空");
//
//    Integer personId = Integer.valueOf(personIdStr);
//    Enabled enabled = Enabled.ENABLED;
//
//    if(!StringUtils.isEmpty(isEnabled)) {
//      enabled = Enum.valueOf(Enabled.class, isEnabled.trim());
//    }
//
//    List<String> result = mobileService.getDesensitizeInfo(personId, enabled);
//
//    return new HttpResult<>(HttpStatus.OK,"成功",result);
//  }
}