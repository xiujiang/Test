/*
 * Copyright 2017 - 数多科技
 * 
 * 北京数多信息科技有限公司
 * 本公司保留所有下述内容的权利。
 * 本内容为保密信息，仅限本公司内部使用。
 * 非经本公司书面许可，任何人不得外泄或用于其他目的。
 */
package com.digibig.service.person.api.remote.hystrix;


import com.digibig.service.person.api.remote.RegionTemplateRemote;
import com.digibig.spring.api.HttpResult;
import com.digibig.spring.api.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;

@Component
public class RegionTemplateRemoteHystrix implements RegionTemplateRemote {

  @Override
  public HttpResult<String> path(@PathVariable("nid") Integer nid) {
    return new HttpResult<>(HttpStatus.SERVER_ERROR, "调用Region-Service失败");
  }

}
