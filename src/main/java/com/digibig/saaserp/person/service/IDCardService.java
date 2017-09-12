/*
 * Copyright 2017 - 数多科技
 * 
 * 北京数多信息科技有限公司
 * 本公司保留所有下述内容的权利。
 * 本内容为保密信息，仅限本公司内部使用。
 * 非经本公司书面许可，任何人不得外泄或用于其他目的。
 */
package com.digibig.saaserp.person.service;

import com.digibig.saaserp.person.domain.IDCard;

public interface IDCardService {
  
  Integer addIdCard(IDCard idCard,Boolean defaultObj) ;
  
  Boolean setCardPicture(Integer personId,Integer idCardId,Integer frontPic,Integer backPic) ;

}
