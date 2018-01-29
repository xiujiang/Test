/*
 * Copyright 2017 - 数多科技
 *
 * 北京数多信息科技有限公司 本公司保留所有下述内容的权利。
 * 本内容为保密信息，仅限本公司内部使用。
 * 非经本公司书面许可，任何人不得外泄或用于其他目的。
 */
package com.digibig.service.person.service;

import com.digibig.commons.util.MaskedUtil;
import com.digibig.service.person.common.CommonParam;
import com.digibig.service.person.domain.Person;
import com.digibig.service.person.enums.IDCardType;
import com.digibig.spring.credential.CredentialHelper;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import com.digibig.spring.service2.AbstractServiceForBase;

@Service
@Validated
public class PersonService extends AbstractServiceForBase<Person> {

  Logger logger = LoggerFactory.getLogger(this.getClass());

  //授权有效时间
  private static final int EXPIRE_TIME = 120;

  @Autowired
  private CredentialHelper credentialHelper;

  @Override
  protected void preAdd(Person person){
    if(Objects.isNull(person.getIdType())){
      person.setIdType(IDCardType.SECOND);
    }
  }

  @Override
  protected void postGet(Person person){
    person.setCredential(this.getCredential());
  }

  @Override
  protected void postAdd(Person person){
    person.setCredential(this.getCredential());
  }

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

  /*
 * 获取授权
 */
  private String getCredential() {
    return credentialHelper.issue(credentialHelper.newCredential(), EXPIRE_TIME);
  }


}