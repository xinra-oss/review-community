package com.xinra.reviewcommunity.rest;

import org.springframework.web.context.request.RequestContextHolder;

public class FrontendUtil {
  
  private FrontendUtil() {}
  
  /**
   * Returns whether the current thread is a web request.
   */
  // TODO: move to nucleus-frontend
  public static boolean isRequest() {
    return RequestContextHolder.getRequestAttributes() != null;
  }

}
