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
  
  /**
   * 绑定身份证
   * @param idCard 身份证
   * @param isDefault 是否默认
   * @return 身份证id
   */
  Integer addIdCard(IDCard idCard,Boolean isDefault) ;
  
  /**
   * 设置身份证图片
   * @param personId 自然人id
   * @param idCardId 身份证id
   * @param frontPic 正面图片
   * @param backPic 背面图片
   * @return 操作结果
   */
  Boolean setCardPicture(Integer personId,Integer idCardId,Integer frontPic,Integer backPic) ;

}
