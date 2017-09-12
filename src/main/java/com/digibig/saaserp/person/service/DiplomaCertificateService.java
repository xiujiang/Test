/*
 * Copyright 2017 - 数多科技
 * 
 * 北京数多信息科技有限公司
 * 本公司保留所有下述内容的权利。
 * 本内容为保密信息，仅限本公司内部使用。
 * 非经本公司书面许可，任何人不得外泄或用于其他目的。
 */
package com.digibig.saaserp.person.service;

import java.util.List;

import com.digibig.saaserp.person.domain.DiplomaCertificate;
import com.digibig.saaserp.person.domain.SchoolRecord;
import com.digibig.saaserp.person.utils.Enabled;

public interface DiplomaCertificateService {
  
  Integer addDiplomaCertificate(DiplomaCertificate diploma) ;
  
  Boolean setDiplomaCertificate(DiplomaCertificate diploma) ;
  
  Boolean setEnabled(Integer personId,Integer certificateId,Enabled enabled) ;
  
  Integer addSchoolRecord(SchoolRecord record) ;
  
  Boolean setSchoolRecord(SchoolRecord record) ;
    
  Boolean setRecordEnabled(Integer personId,Integer recordId,Enabled enabled) ;
  
  Boolean addCheckReport(Integer personId,Integer recordId,Integer reportFile) ;
  
  List<SchoolRecord> getSchoolRecord(Integer personId);

}
