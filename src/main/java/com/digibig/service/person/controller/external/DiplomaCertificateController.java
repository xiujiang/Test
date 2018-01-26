/*
 * Copyright 2017 - 数多科技
 * 
 * 北京数多信息科技有限公司
 * 本公司保留所有下述内容的权利。
 * 本内容为保密信息，仅限本公司内部使用。
 * 非经本公司书面许可，任何人不得外泄或用于其他目的。
 */
package com.digibig.service.person.controller.external;

import com.digibig.service.person.domain.DiplomaCertificate;
import com.digibig.spring.api.HttpResult;
import com.digibig.spring.api.HttpStatus;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.digibig.service.person.service.DiplomaCertificateService;



@RestController("DiplomaCertificateController-e")
@RequestMapping("/v1.0/person/diploma/certificate")
public class DiplomaCertificateController {

  @Autowired
  private com.digibig.service.person.controller.internal.DiplomaCertificateController certificateController;
  
  private Logger logger = LoggerFactory.getLogger(getClass());
  
  @Autowired
  private DiplomaCertificateService certificateService;

  @PostMapping("/add")
  public HttpResult<DiplomaCertificate> addDiplomaCertificate(@RequestBody DiplomaCertificate diplomaCertificate){

    return certificateController.addJson(diplomaCertificate);
  }
  

  @PostMapping("update")
  public HttpResult<DiplomaCertificate> setDiplomaCertificate(@RequestBody DiplomaCertificate diplomaCertificate){

    return certificateController.updateSelectiveJson(diplomaCertificate);
  }

//  @PostMapping("/enabled")
//  public HttpResult<Boolean> setCertificateEnabled(@RequestBody Map<String, String> paramMap){
//
//    Assert.isTrue(!StringUtils.isEmpty(paramMap.get(CommonParam.MAP_PARAM_PERSONID)), "设置学位证书信息的有效性personId不能为空");
//    Assert.isTrue(!StringUtils.isEmpty(paramMap.get(CERTIFICATE_ID)), "certificateId不能为空");
//    Assert.isTrue(!StringUtils.isEmpty(paramMap.get(CommonParam.MAP_PARAM_ENABLED)), "enabled不能为空");
//
//    Integer personId = Integer.valueOf(paramMap.get(CommonParam.MAP_PARAM_PERSONID));
//    Integer certificateId = Integer.valueOf(paramMap.get(CERTIFICATE_ID));
//    Enabled enabled = Enum.valueOf(Enabled.class, paramMap.get(CommonParam.MAP_PARAM_ENABLED).trim());
//
//    Boolean result = certificateService.setEnabled(personId,certificateId,enabled);
//
//    if(result) {
//      return new HttpResult<>(HttpStatus.OK,"成功",result);
//    }
//    return new HttpResult<>(HttpStatus.SERVER_ERROR,"失败",result);
//  }

  
  /**
   * <p>
   * 查询自然人学历信息
   * </p>
   * @param personId 自然人id
   * @return 学历信息
   */
  @GetMapping("/list")
  public HttpResult<List<DiplomaCertificate>> getSchoolRecord(@RequestParam("personId") Integer personId){

    return new HttpResult<>(HttpStatus.OK,"成功",certificateService.listWithParent(personId));
  }
  
}
