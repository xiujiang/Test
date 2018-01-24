/*
 * Copyright 2017 - 数多科技
 *
 * 北京数多信息科技有限公司 本公司保留所有下述内容的权利。
 * 本内容为保密信息，仅限本公司内部使用。
 * 非经本公司书面许可，任何人不得外泄或用于其他目的。
 */
package com.digibig.service.person.service;

import com.digibig.commons.api.HttpResult;
import com.digibig.commons.constant.Charset;
import com.digibig.commons.constant.HttpMethod;
import com.digibig.commons.enums.AuthMethodEnum;
import com.digibig.commons.enums.AuthOperEnum;
import com.digibig.commons.exception.DigibigException;
import com.digibig.commons.util.HttpClient;
import com.digibig.commons.util.MaskedUtil;
import com.digibig.service.person.common.CommonParam;
import com.digibig.service.person.domain.Person;
import com.digibig.spring.credential.Credential;
import com.digibig.spring.credential.CredentialHelper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import com.digibig.spring.service2.AbstractServiceForBase;

@Service
@Validated
public class PersonService extends AbstractServiceForBase<Person> {

  Logger logger = LoggerFactory.getLogger(this.getClass());

  public PersonService() {
    super(Person.class);
  }

  private Person getDesensitizeInfo(Person person) {
    //脱敏处理
    String mobile = person.getMobile();
    if (!StringUtils.isEmpty(mobile)) {
      person.setMobile(
          MaskedUtil.masked(mobile, CommonParam.MOBILE_DES_PRE, CommonParam.MOBILE_DES_SUF));
    }

    String idsCard = person.getIdNumber();
    if (!StringUtils.isEmpty(idsCard)) {
      person.setIdNumber(
          MaskedUtil.masked(idsCard, CommonParam.IDCARD_DES_PRE, CommonParam.IDCARD_DES_SUF));
    }

    String email = person.getEmail();
    if (!StringUtils.isEmpty(email)) {
      Integer suf = email.length() - email.lastIndexOf('@');
      person.setEmail(MaskedUtil.masked(email, CommonParam.EMIAL_DES_PRE, suf));
    }
    return person;
  }

  /*
   * 获取自然人脱敏信息
   */
  public Person getDesensitizeInfo(Integer personId) {

    Person person = this.get(personId);

    return this.getDesensitizeInfo(person);
  }


}