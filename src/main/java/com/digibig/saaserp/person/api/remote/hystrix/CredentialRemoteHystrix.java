/*
 * Copyright 2017 - 数多科技
 * 
 * 北京数多信息科技有限公司
 * 本公司保留所有下述内容的权利。
 * 本内容为保密信息，仅限本公司内部使用。
 * 非经本公司书面许可，任何人不得外泄或用于其他目的。
 */
package com.digibig.saaserp.person.api.remote.hystrix;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.digibig.saaserp.commons.api.HttpResult;
import com.digibig.saaserp.commons.constant.HttpStatus;
import com.digibig.saaserp.person.api.remote.CredentialRemote;

@Component
public class CredentialRemoteHystrix implements CredentialRemote {
  
  private Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  public HttpResult<String> submit(Map<String, String> params) {
    logger.error("调用提交凭据接口失败！");
    return new HttpResult<>(HttpStatus.SERVER_ERROR, "调用提交凭据接口失败！");
  }

  @Override
  public HttpResult<Void> verify(String credential) {
    logger.error("调用验证凭证接口失败！");
    return new HttpResult<>(HttpStatus.SERVER_ERROR, "调用验证凭证接口失败！");
  }

}
