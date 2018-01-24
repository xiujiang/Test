/*
 * Copyright 2017 - 数多科技
 *
 * 北京数多信息科技有限公司 本公司保留所有下述内容的权利。
 * 本内容为保密信息，仅限本公司内部使用。
 * 非经本公司书面许可，任何人不得外泄或用于其他目的。
 */
package com.digibig.service.person.service;

import com.digibig.service.person.domain.DiplomaCertificate;
import com.digibig.service.person.domain.SchoolRecord;
import com.digibig.spring.service2.AbstractServiceForItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class SchoolRecordService extends AbstractServiceForItem<SchoolRecord> {

  Logger logger = LoggerFactory.getLogger(this.getClass());

  public SchoolRecordService() {
    super(SchoolRecord.class, "personId");
  }

  @Override
  protected void preAdd(SchoolRecord schoolRecord){
    this.checkSchoolRecord(schoolRecord);
  }

  private void checkSchoolRecord(SchoolRecord schoolRecord) {

    SchoolRecord example = new SchoolRecord();
    example.setPersonId(schoolRecord.getPersonId());
    example.setCertificateNumber(schoolRecord.getCertificateNumber());

    Assert.isTrue(!CollectionUtils.isEmpty(this.queryAll(example)),"该证书已存在。");
  }

}