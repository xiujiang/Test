/*
 * Copyright 2017 - 数多科技
 * 
 * 北京数多信息科技有限公司
 * 本公司保留所有下述内容的权利。
 * 本内容为保密信息，仅限本公司内部使用。
 * 非经本公司书面许可，任何人不得外泄或用于其他目的。
 */
package com.digibig.saaserp.person.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.digibig.saaserp.commons.exception.DBException;
import com.digibig.saaserp.commons.exception.DigibigException;
import com.digibig.saaserp.person.api.remote.RegionTemplateRemote;
import com.digibig.saaserp.person.domain.Address;
import com.digibig.saaserp.person.domain.AddressExample;
import com.digibig.saaserp.person.mapper.AddressMapper;
import com.digibig.saaserp.person.service.AddressService;
import com.digibig.saaserp.person.service.PersonService;
import com.digibig.saaserp.person.utils.AddressType;
import com.digibig.saaserp.person.utils.Enabled;

@Service
public class AddressServiceImpl implements AddressService {
  
  private Logger logger = LoggerFactory.getLogger(getClass());
  
  @Autowired
  private AddressMapper addressMapper;
  
  @Autowired
  private PersonService personService;
  
  @Autowired
  private RegionTemplateRemote regionTemplateRemote;

  /**
   * 添加地址信息
   */
  @Transactional
  @Override
  public Integer addAddress(Address address, Boolean isDefault) {
    
    Address addr = checkAddress(address.getPersonId(),address.getLastNode(),address.getDetailAddress());
    Integer addressId = null;
    //不存在
    if(addr == null) {
      
      try {
        addressMapper.insertSelective(address);
      }catch(RuntimeException e) {
        throw new DBException("数据库操作异常",e);
      }
      
      addressId = address.getId();
    }else {
      //存在,判断是否有效，无效则设置为有效状态
      if(addr.getEnabled() == Enabled.NOT_ENABLED.getValue()) {
        
        setEnabled(addr.getPersonId(),addr.getId(),Enabled.ENABLED);
        
        addressId = addr.getId();
      }else {
        return null;
      }
    }
    
    if(isDefault) {
      Boolean result = personService.setDefaultAddress(address.getPersonId(), addressId);
      
      if(!result) {
        logger.error("设置默认地址失败");
        return null;
      }
    }
    
    return addressId;
  }

  /**
   * 设置地址的有效性
   */
  @Transactional
  @Override
  public Boolean setEnabled(Integer personId, Integer addressId, Enabled enabled){
    
    AddressExample example = new AddressExample();
    example.createCriteria().andIdEqualTo(addressId).andPersonIdEqualTo(personId);
    
    //设置有效性标识
    Address address = new Address();
    address.setEnabled(enabled.getValue());
    address.setLastTime(new Date());
    Integer rows = null;
    
    try {
      rows = addressMapper.updateByExampleSelective(address, example);
    }catch(RuntimeException e) {
      throw new DBException("数据库操作异常",e);
    }
    return rows>0;
  }

  /**
   * 获取自然人地址列表
   * @throws DigibigException 
   */
  @Override
  public List<Map<String ,String>> getAddresses(Integer personId, Enabled enabled) throws DigibigException {
    
    // 查询地址列表
    AddressExample example = new AddressExample();
    example.createCriteria().andPersonIdEqualTo(personId).andEnabledEqualTo(enabled.getValue());
    
    List<Address> addresses = addressMapper.selectByExample(example);
    
    List<Map<String ,String>> resultList = new ArrayList<>();
//    Map<String ,String> resultMap = new HashMap<>();
    //查询节点的地址信息 -- 图库
    for(Address addr : addresses) {
      
      Map<String ,String> resultMap = new HashMap<>();
      
      resultMap.put("id", addr.getId().toString());
      resultMap.put("address", toAddress(addr.getLastNode(),addr.getDetailAddress()));
      resultMap.put("addressType", AddressType.getName(addr.getAddressType()));
      resultMap.put("addressAlias", addr.getAddressAlias());
      
      resultList.add(resultMap);
    }
    //拼装返回信息并返回
    return resultList;
  }
  
  /**
   * 通过id获取地址信息
   * @throws DigibigException 
   */
  @Override
  public String getAddressById(Integer id) throws DigibigException {
    Address address = addressMapper.selectByPrimaryKey(id);
    return toAddress(address.getLastNode(),address.getDetailAddress());
  }
  
  /**
   * 通过最后节点和详细地址获取地址信息
   * @param node 最后节点id
   * @param detail 详细地址
   * @return 地址信息
   * @throws DigibigException
   */
  private String toAddress(Integer node,String detail) throws DigibigException {
    String preAddress = regionTemplateRemote.path(node).getData();
    if(preAddress == null) {
      throw new DigibigException("地址节点路径信息查询失败");
    }
    return preAddress.concat(detail);
  }

  /**
   * 通过自然人id，最后节点，详细地址查询地址信息
   * @param personId 自然人id
   * @param lastNode 最后节点
   * @param detailAddress 详细信息
   * @return 返回地址
   */
  private Address checkAddress(Integer personId ,Integer lastNode, String detailAddress) {
    
    AddressExample example = new AddressExample();
    example.createCriteria().andPersonIdEqualTo(personId).andLastNodeEqualTo(lastNode).andDetailAddressEqualTo(detailAddress);
    
    List<Address> addresses = addressMapper.selectByExample(example);
    if(addresses.size() == 0) {
      return null;
    }
    return addresses.get(0);
  }
}
