/*
 * Copyright 2017 - 数多科技
 * 
 * 北京数多信息科技有限公司
 * 本公司保留所有下述内容的权利。
 * 本内容为保密信息，仅限本公司内部使用。
 * 非经本公司书面许可，任何人不得外泄或用于其他目的。
 */
package com.digibig.saaserp.person.api.remote;

import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.digibig.saaserp.commons.api.HttpResult;
import com.digibig.saaserp.person.api.remote.hystrix.CredentialRemoteHystrix;

@FeignClient(name = "credential-service", fallback = CredentialRemoteHystrix.class)
public interface CredentialRemote {

  @RequestMapping(value = "/v1.0/credential")
  public HttpResult<String> submit(Map<String, String> params);
  
  @RequestMapping(value = "/v1.0/credential/{credential}", method = RequestMethod.PUT)
  public HttpResult<Void> verify(@PathVariable("credential") String credential);
}
