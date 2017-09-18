/*
 * Copyright 2017 - 数多科技
 * 
 * 北京数多信息科技有限公司 本公司保留所有下述内容的权利。 本内容为保密信息，仅限本公司内部使用。 非经本公司书面许可，任何人不得外泄或用于其他目的。
 */
package com.digibig.saaserp.person.aop;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * <p>
 * 该类为AOP中的Aspect（切面类），主要处理各子服务间API调用的日志记录,包括输入参数、输出参数、执行时长等等
 * </p>
 * 
 * @author zhangmingming<zhangmingming01@we.com>
 * @datetime 2017年8月18日 下午5:59:18
 * @version v1.0
 * @since 1.8
 */
@Aspect
@Component
public class ApiLogAspect {

  private Logger logger = LoggerFactory.getLogger(getClass());

  /**
   * <p>
   * 切入点:所有controller的所有方法
   * </p>
   */
  @Pointcut("execution(public * com.digibig.saaserp.calendar.controller..*.*(..))")
  public void webLog() {
    if (logger.isDebugEnabled()) {
      logger.debug("a weblog will be print");
    }
  }

  /**
   * <p>
   * 目标方法执行前
   * </p>
   * 
   * @param joinPoint：切入点
   */
  @Before("webLog()")
  public void doBefore(JoinPoint joinPoint) {
    ServletRequestAttributes attributes =
        (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    HttpServletRequest request = attributes.getRequest();
    logger.info("url : {}", request.getRequestURL());
    logger.info("request method : {}", request.getMethod());
    logger.info("remote ip : {}", request.getRemoteAddr());
    logger.info("class method : {}",
        joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
    logger.info("request params : {}", joinPoint.getArgs());
  }

  /**
   * <p>
   * 目标方法执行之后
   * </p>
   * 
   * @param ret
   */
  @AfterReturning(returning = "ret", pointcut = "webLog()")
  public void doAfterReturning(Object ret) {
    logger.info("response : {}", ret);
  }

  /**
   * <p>
   * 环绕通知
   * </p>
   * 
   * @param pjp:切入点
   * @throws Throwable
   */
  @Around(value = "webLog()")
  public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
    long startTime = System.currentTimeMillis();
    Object result = pjp.proceed();
    long endTime = System.currentTimeMillis();
    logger.info("{} execute time :{}ms", pjp.getSignature(), endTime - startTime);
    return result;
  }

  @AfterThrowing(pointcut = "webLog()", throwing = "ex")
  public void doAfterThrowing(Throwable ex) {
    logger.error(ex.getMessage(), ex);
  }
}
