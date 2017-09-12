/*
 * Copyright 2017 - 数多科技
 * 
 * 北京数多信息科技有限公司 本公司保留所有下述内容的权利。 本内容为保密信息，仅限本公司内部使用。 非经本公司书面许可，任何人不得外泄或用于其他目的。
 */
package com.digibig.saaserp.person.config;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.digibig.saaserp.commons.api.HttpResult;
import com.digibig.saaserp.commons.constant.HttpStatus;

/**
 * <p>
 * 全局异常处理
 * </p>
 * 
 * @author zhangmingming<zhangmingming01@we.com>
 * @datetime 2017年8月31日 上午11:16:54
 * @version v1.0
 * @since 1.8
 */
@ControllerAdvice
public class WebControllerAdvice {

  /**
   * 全局异常捕捉处理
   * 
   * @param ex
   * @return
   */
  @ResponseBody
  @ExceptionHandler(value = Exception.class)
  public HttpResult<Void> globalErrorHandler(Exception e) {
    return new HttpResult<>(HttpStatus.SERVER_ERROR, e.getMessage());
  }

  /**
   * web层异常捕捉处理
   * 
   * @param ex
   * @return
   */
  @ResponseBody
  @ExceptionHandler(value = IllegalArgumentException.class)
  public HttpResult<Void> webErrorHandler(IllegalArgumentException e) {
    return new HttpResult<>(HttpStatus.PARAM_ERROR, e.getMessage());
  }


}
