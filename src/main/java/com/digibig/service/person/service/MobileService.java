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
import com.digibig.service.person.domain.Mobile;
import com.digibig.service.person.enums.Status;
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
public class MobileService extends AbstractServiceForItem<Mobile> {

  Logger logger = LoggerFactory.getLogger(this.getClass());

  public MobileService() {
    super(Mobile.class, "personId");
  }

  @Override
  protected void preAdd(Mobile mobile){
    this.checkMobile(mobile);
  }

  @Override
  protected void switchStatusInternal(Mobile address,String statusCode){
    Status status = Status.fromString(statusCode);
    address.setStatus(status);
  }

  /**
   * 获取脱敏的手机号数据
   * @param personId
   * @return
   */
  public List<Mobile> getDesensitizeInfo(Integer personId) {
    //查询数据
    List<Mobile> mobiles = this.listWithParent(personId);
    //脱敏处理
    for(Mobile mobile : mobiles) {
      String number = MaskedUtil.masked(mobile.getNumber(), CommonParam.MOBILE_DES_PRE, CommonParam.MOBILE_DES_SUF);
      mobile.setNumber( number);
    }
    return mobiles;
  }

  private void checkMobile(Mobile mobile){
    Mobile example = new Mobile();
    example.setPersonId(mobile.getPersonId());
    example.setNumber(mobile.getNumber());
    Assert.isTrue(CollectionUtils.isEmpty(this.queryAll(example)),"该手机号已存在。");
  }

}