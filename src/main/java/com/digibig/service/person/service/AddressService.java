/*
 * Copyright 2017 - 数多科技
 *
 * 北京数多信息科技有限公司 本公司保留所有下述内容的权利。
 * 本内容为保密信息，仅限本公司内部使用。
 * 非经本公司书面许可，任何人不得外泄或用于其他目的。
 */
package com.digibig.service.person.service;

import com.digibig.service.person.api.remote.RegionTemplateRemote;
import com.digibig.service.person.domain.Address;
import com.digibig.spring.exception.DigibigException;
import com.digibig.spring.service2.AbstractServiceForItem;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
public class AddressService extends AbstractServiceForItem<Address> {

  Logger logger = LoggerFactory.getLogger(this.getClass());

  public AddressService() {
    super(Address.class, "personId");
  }

  @Autowired
  private RegionTemplateRemote regionTemplateRemote;

  @Override
  protected void preAdd(Address address){
    this.checkAddress(address);
  }

  @Override
  protected void postGet(Address address){
    this.toAddress(address.getLastNode(), address.getDetailAddress());

  }

  /**
   * 通过最后节点和详细地址获取地址信息
   * @param node 最后节点id
   * @param detail 详细地址
   * @return 地址信息
   * @throws DigibigException
   */
  private String toAddress(Integer node,String detail){
    String preAddress = regionTemplateRemote.path(node).getData();
    Assert.isTrue(!StringUtils.isEmpty(preAddress),"地址节点路径信息查询失败");
    return preAddress.concat(detail);
  }


  private void checkAddress(Address address) {

    Address example = new Address();
    example.setPersonId(address.getPersonId());
    example.setLastNode(address.getLastNode());
    example.setDetailAddress(address.getDetailAddress());
    List<Address> addresses = this.queryAll(example);
    Assert.isTrue(CollectionUtils.isEmpty(addresses),"该地址已存在");

  }
}