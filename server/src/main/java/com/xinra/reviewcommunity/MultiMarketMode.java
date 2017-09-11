package com.xinra.reviewcommunity;

/**
 * At startup, a bean of this type is created to indicate whether the application runs
 * in multi or single market mode. In multi market mode there are different ways
 * to detect the current market.
 */
public enum MultiMarketMode {
  
  /**
   * Market is determined by subdomain, e.g<br>
   * de.example.com<br>
   * us.example.com
   */
  SUBDOMAIN,
  
  /**
   * Market is determined by path, e.g.<br>
   * example.com/de<br>
   * example.com/us
   */
  PATH,
  
  /**
   * There is only a single market.
   */
  DISABLED;
  
  public boolean isEnabled() {
    return this != DISABLED;
  }
  
}
