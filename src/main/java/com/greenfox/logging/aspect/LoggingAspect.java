package com.greenfox.logging.aspect;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Service
public class LoggingAspect {

  Logger logger = LoggerFactory.getLogger(this.getClass());

  @Pointcut("execution(@org.springframework.web.bind.annotation.*Mapping * *(..))")
  public void restController() {
  }

  @After("restController()")
  public void logInfoAdvice() {
    logger.info(getRequest().getServerName() + " / HTTP-REQUEST " + getRequest().getRequestURI());
  }

  @After("restController()")
  public void logErrorAdvice() {
    if (getResponse().getStatus() == HttpServletResponse.SC_BAD_REQUEST || getRequest()
        .getRequestURI().equals("/error")) {
      logger.error(getRequest().getServerName() + " / HTTP-ERROR " + getRequest().getRequestURI());
    }
  }

  @After("restController()")
  public void logAfterThrowingAdvice() {
    HttpStatus status = HttpStatus.valueOf(getResponse().getStatus());
    if (status.is4xxClientError() || status.is5xxServerError()) {
      logger.error(
          "Error status : " + status + " " + getRequest().getRequestURI());
    }
  }

  public HttpServletResponse getResponse() {
    HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder
        .getRequestAttributes()).getResponse();
    return response;
  }

  public HttpServletRequest getRequest() {
    HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
        .getRequestAttributes()).getRequest();
    return request;
  }
}
