/*
 * Copyright 2017 - 数多科技
 * 
 * 北京数多信息科技有限公司
 * 本公司保留所有下述内容的权利。
 * 本内容为保密信息，仅限本公司内部使用。
 * 非经本公司书面许可，任何人不得外泄或用于其他目的。
 */
package com.digibig.saaserp.person.aop;

import java.lang.reflect.Method;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import com.digibig.saaserp.commons.api.HttpResult;
import com.digibig.saaserp.commons.constant.HttpStatus;
import com.digibig.saaserp.person.utils.JsonValidator;

@Aspect
@Component
public class JsonValidateAspect {

  /**
   * <p>
   * 切入点:所有添加@JsonValidator注解的controller方法
   * </p>
   */
  @Pointcut("execution(public * com.digibig.saaserp.person.controller..*.*(..))"
      + " && @annotation(com.digibig.saaserp.person.utils.JsonValidator)")
  public void JsonParamValidate() {}
  
  /**
   * <p>
   * 环绕通知
   * </p>
   * 
   * @param pjp:切入点
   * @throws Throwable
   */
  @Around(value = "JsonParamValidate()")
  public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
    MethodSignature signature = (MethodSignature) pjp.getSignature();  
    //获取被拦截的方法  
    Method method = signature.getMethod();
      
    HttpResult<Void> bindingResult = new HttpResult<>(HttpStatus.OK, "参数校验成功");
    
    Object[] args = pjp.getArgs();
    
    for (Object arg : args) {
      if (arg instanceof Map<?, ?>) {
        @SuppressWarnings("unchecked")
        Map<String, String> map = (Map<String, String>) arg;
        JsonValidator jsonValidator = method.getAnnotation(JsonValidator.class);  
        String[] requiredArgs = jsonValidator.required().split(",");
        bindingResult = this.validate(requiredArgs, map);
      }
    }
    if (bindingResult.getCode() != HttpStatus.OK) {
      return bindingResult;
    }
    Object result = pjp.proceed();
    return result;
  }
  
  private HttpResult<Void> validate(String[] requiredArgs, Map<String, String> notifyMap) {
    for (String arg : requiredArgs) {
      if (StringUtils.isEmpty(notifyMap.get(arg.trim()))) {
        return new HttpResult<>(HttpStatus.PARAM_ERROR, "请求参数" + arg + "不能为空！");
      }
    }
    return new HttpResult<>(HttpStatus.OK, "参数校验成功");
  }
}
