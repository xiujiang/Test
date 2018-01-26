/*
 * Copyright 2017 - 数多科技
 * 
 * 北京数多信息科技有限公司
 * 本公司保留所有下述内容的权利。
 * 本内容为保密信息，仅限本公司内部使用。
 * 非经本公司书面许可，任何人不得外泄或用于其他目的。
 */
package com.digibig.service.person.remote;

import com.digibig.service.person.domain.AuthResult;
import feign.Headers;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "auth", url = "http://idcard.market.alicloudapi.com")
public interface AuthorizationRemote {

  @RequestMapping(value = "/lianzhuo/idcard", method = RequestMethod.GET)
  AuthResult auth(@RequestParam("cardno") String cardno,
      @RequestParam("name") String name,@RequestHeader("Authorization") String header);
  
}