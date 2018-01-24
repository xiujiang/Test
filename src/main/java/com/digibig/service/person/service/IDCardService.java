/*
 * Copyright 2017 - 数多科技
 *
 * 北京数多信息科技有限公司 本公司保留所有下述内容的权利。
 * 本内容为保密信息，仅限本公司内部使用。
 * 非经本公司书面许可，任何人不得外泄或用于其他目的。
 */
package com.digibig.service.person.service;

import com.digibig.commons.exception.DBException;
import com.digibig.service.person.common.CommonParam;
import com.digibig.service.person.domain.IDCard;
import com.digibig.spring.service2.AbstractServiceForItem;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class IDCardService extends AbstractServiceForItem<IDCard> {

  Logger logger = LoggerFactory.getLogger(this.getClass());

  public IDCardService() {
    super(IDCard.class, "personId");
  }

  @Override
  protected void preAdd(IDCard idCard){
    checkIDCard(idCard);
  }

  /**
   * 通过唯一码获取身份证
   * @param idCard 唯一码
   * @return 身份证
   */
  private void checkIDCard(IDCard idCard) {
    IDCard example = new IDCard();
    example.setUniqueCode(idCard.getUniqueCode());

    Assert.isTrue(CollectionUtils.isEmpty(this.queryAll(example)),"该身份证已存在。");
  }
}