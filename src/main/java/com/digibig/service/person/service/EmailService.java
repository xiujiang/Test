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
import com.digibig.service.person.domain.Email;
import com.digibig.service.person.enums.Status;
import com.digibig.spring.service2.AbstractServiceForItem;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class EmailService extends AbstractServiceForItem<Email> {

  Logger logger = LoggerFactory.getLogger(this.getClass());

  public EmailService() {
    super(Email.class, "personId");
  }


  @Autowired
  private PersonService personService;

  @Override
  protected void preAdd(Email email){
    this.checkEmail(email);
  }

  @Override
  protected void switchStatusInternal(Email address,String statusCode){
    Status status = Status.fromString(statusCode);
    address.setStatus(status);
  }

  private void checkEmail(Email email){
    Email example = new Email();
    example.setPersonId(email.getPersonId());
    example.setEmail(email.getEmail());
    Assert.isTrue(CollectionUtils.isEmpty(this.queryAll(example)),"该邮箱已存在。");
  }


  /**
   * 查询邮箱列表 - 脱敏
   */
  public List<Email> getDesensitizeInfo(Integer personId) {
    //查询数据
    List<Email> emails = this.listWithParent(personId);
    //脱敏处理
    int suf = 0;
    for(Email email : emails) {
      //对list重新设置或者可以返回新的list
      suf = email.getEmail().length() - email.getEmail().lastIndexOf('@');
      String em = MaskedUtil.masked(email.getEmail(), CommonParam.EMIAL_DES_PRE, suf);
      email.setEmail(em);
    }
    return emails;
  }
}