package com.xinra.reviewcommunity;

import com.xinra.nucleus.common.ContextHolder;
import com.xinra.nucleus.common.GenericContextHolder;
import com.xinra.reviewcommunity.rest.FrontendUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@Slf4j
@SpringBootApplication
@ComponentScan({"com.xinra.reviewcommunity", "com.xinra.nucleus"})
public class ReviewCommunityApplication {
  
  public static void main(String[] args) {
    SpringApplication.run(ReviewCommunityApplication.class, args);
  }
  
  @Value("${reviewcommunity.multi-market-mode:disabled}")
  private String multiMarketMode;
  
  /**
   * Sets the multi market mode according to configuration.
   * Default is disabled.
   */
  @Bean
  public MultiMarketMode multiMarketMode() {
    MultiMarketMode mmm;
//    try {
//      mmm = MultiMarketMode.valueOf(multiMarketMode.trim().toUpperCase());
//    } catch (IllegalArgumentException | NullPointerException ex) {
//      mmm = MultiMarketMode.DISABLED;
//    }
    mmm = MultiMarketMode.PATH;
    log.info("Multi market mode is {}.", mmm);
    return mmm;
  }
  
  // generic type <?> is used to be able to override this bean to hold a subclass of Context
  @Bean
  public ContextHolder<?> contextHolder() {
    return new GenericContextHolder<>(Context.class, Context::new, FrontendUtil::isRequest);
  }
}
