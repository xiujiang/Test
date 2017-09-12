/*
 * Copyright 2017 - 数多科技
 * 
 * 北京数多信息科技有限公司
 * 本公司保留所有下述内容的权利。
 * 本内容为保密信息，仅限本公司内部使用。
 * 非经本公司书面许可，任何人不得外泄或用于其他目的。
 */
package com.digibig.saaserp.person.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.digibig.saaserp.commons.exception.DBException;
import com.digibig.saaserp.person.domain.DiplomaCertificate;
import com.digibig.saaserp.person.domain.DiplomaCertificateExample;
import com.digibig.saaserp.person.domain.SchoolRecord;
import com.digibig.saaserp.person.domain.SchoolRecordExample;
import com.digibig.saaserp.person.mapper.DiplomaCertificateMapper;
import com.digibig.saaserp.person.mapper.SchoolRecordMapper;
import com.digibig.saaserp.person.service.DiplomaCertificateService;
import com.digibig.saaserp.person.utils.Enabled;
@Service
public class DiplomaCertificateServiceImpl implements DiplomaCertificateService {

  @Autowired
  private DiplomaCertificateMapper diplomaMapper;
  
  @Autowired
  private SchoolRecordMapper schoolRecordMapper;
  
  private DiplomaCertificate getDiploma(Integer personId , String certificateNo) {
    DiplomaCertificateExample example = new DiplomaCertificateExample();
    example.createCriteria().andPersonIdEqualTo(personId).andCertificateNumberEqualTo(certificateNo);
    List<DiplomaCertificate> diplomas = diplomaMapper.selectByExample(example);
    if(diplomas.size() == 0) {
      return null;
    }
    return diplomas.get(0);
  }
  

  
  @Override
  @Transactional
  public Integer addDiplomaCertificate(DiplomaCertificate diploma) {
    
    DiplomaCertificate aDiploma = getDiploma(diploma.getPersonId(), diploma.getCertificateNumber());

    if(aDiploma == null) {
      
      try {
        diplomaMapper.insertSelective(diploma);
      }catch(RuntimeException e) {
        throw new DBException("数据库操作异常", e);
      }
      return diploma.getId();
      
    }else {
      
      if(aDiploma.getEnabled() == Enabled.NOT_ENABLED.getValue()) {
        
        diploma.setId(aDiploma.getId());
        diploma.setEnabled(Enabled.ENABLED.getValue());
        this.setDiplomaCertificate(diploma);
        return diploma.getId();
      }
      return null;
    }
  }

  @Transactional
  @Override
  public Boolean setDiplomaCertificate(DiplomaCertificate diploma) {

    DiplomaCertificateExample example = new DiplomaCertificateExample();
    example.createCriteria().andIdEqualTo(diploma.getId()).andPersonIdEqualTo(diploma.getPersonId());
    
    Integer rows = null;
    try {
      rows = diplomaMapper.updateByExampleSelective(diploma, example);
    }catch(RuntimeException e) {
      throw new DBException("数据库操作异常", e);
    }

    return rows>0;
  }

  @Transactional
  @Override
  public Boolean setEnabled(Integer personId, Integer certificateId, Enabled enabled) {
    
    DiplomaCertificateExample example = new DiplomaCertificateExample();
    example.createCriteria().andIdEqualTo(certificateId).andPersonIdEqualTo(personId);
    
    DiplomaCertificate diploma = new DiplomaCertificate();
    diploma.setEnabled(enabled.getValue());
    diploma.setLastTime(new Date());
    
    Integer rows = null;
    try {
      rows = diplomaMapper.updateByExampleSelective(diploma, example);
    }catch(RuntimeException e) {
      throw new DBException("数据库操作异常", e);
    }
    
    if(rows == 0) {
      return false;
    }
    return true;
  }

  @Transactional
  @Override
  public Boolean setRecordEnabled(Integer personId, Integer recordId, Enabled enabled) {

    SchoolRecordExample example = new SchoolRecordExample();
    example.createCriteria().andIdEqualTo(recordId).andPersonIdEqualTo(personId);
    SchoolRecord record = new SchoolRecord();
    record.setEnabled(enabled.getValue());
    record.setLastTime(new Date());
    
    Integer rows = null;
    try {
      rows = schoolRecordMapper.updateByExampleSelective(record, example);
    }catch(RuntimeException e) {
      throw new DBException("数据库操作异常", e);
    }
    if(rows == 0) {
      return false;
    }
    return true;
  }

  @Override
  public List<SchoolRecord> getSchoolRecord(Integer personId) {
    
    SchoolRecordExample example = new SchoolRecordExample();
    example.createCriteria().andPersonIdEqualTo(personId).andEnabledEqualTo(Enabled.ENABLED.getValue());
    
    List<SchoolRecord> records = schoolRecordMapper.selectByExample(example);
    
    return records;
  }

  @Transactional
  @Override
  public Integer addSchoolRecord(SchoolRecord SchoolRecord) {

    SchoolRecord record = getRecordByNo(SchoolRecord.getPersonId(),SchoolRecord.getCertificateNumber());
    if(record == null ) {
      
      try {
        schoolRecordMapper.insertSelective(SchoolRecord);
      }catch(RuntimeException e) {
        throw new DBException("数据库操作异常", e);
      }
      
      return SchoolRecord.getId();
      
    }else {
      if(record.getEnabled() == Enabled.NOT_ENABLED.getValue()) {
        
        SchoolRecord.setId(record.getId());
        SchoolRecord.setEnabled(Enabled.ENABLED.getValue());
        this.setSchoolRecord(SchoolRecord);
        
        return record.getId();
      }
      return null;
    }
  }

  @Transactional
  @Override
  public Boolean setSchoolRecord(SchoolRecord record) {
    
    SchoolRecordExample example = new SchoolRecordExample();
    example.createCriteria().andIdEqualTo(record.getId()).andPersonIdEqualTo(record.getPersonId());
    
    Integer rows = null;
    try {
      rows = schoolRecordMapper.updateByExampleSelective(record, example);
    }catch(RuntimeException e) {
      throw new DBException("数据库操作异常", e);
    }

    return rows>0;
  }
  
  private SchoolRecord getRecordByNo(Integer personId,String certificateNo) {
    
    SchoolRecordExample example = new SchoolRecordExample();
    example.createCriteria().andPersonIdEqualTo(personId).andCertificateNumberEqualTo(certificateNo);
    List<SchoolRecord> records = schoolRecordMapper.selectByExample(example);
    
    if(records.size() != 0) {
      return records.get(0);
    }else {
      return null;
    }
  }

  @Transactional
  @Override
  public Boolean addCheckReport(Integer personId, Integer recordId, Integer reportFile) {
    SchoolRecordExample example = new SchoolRecordExample();
    example.createCriteria().andIdEqualTo(recordId).andPersonIdEqualTo(personId);
    
    SchoolRecord record = new SchoolRecord();
    record.setVerificationFile(reportFile);
    
    Integer result = null;
    try {
      result = schoolRecordMapper.updateByExampleSelective(record, example);
    }catch(RuntimeException e) {
      throw new DBException("数据库操作异常", e);
    }

    if(result == 0) {
      return false;
    }
    return true;
  }
  
}
