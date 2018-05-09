/*
 * Copyright 2017 - 数多科技
 * 
 * 北京数多信息科技有限公司
 * 本公司保留所有下述内容的权利。
 * 本内容为保密信息，仅限本公司内部使用。
 * 非经本公司书面许可，任何人不得外泄或用于其他目的。
 */
package com.digibig.service.person.remote;


import com.digibig.service.person.remote.hystrix.RegionTemplateRemoteHystrix;
import com.digibig.spring.api.HttpResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "region-service", fallback = RegionTemplateRemoteHystrix.class)
public interface RegionTemplateRemote {
  @RequestMapping(value = "/v1.0/region-service/path/{nid}", method = RequestMethod.GET)
  HttpResult<String> path(@PathVariable("nid") Integer nid);
  
}