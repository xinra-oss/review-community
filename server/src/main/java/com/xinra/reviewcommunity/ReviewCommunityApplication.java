package com.xinra.reviewcommunity;

import com.xinra.nucleus.common.ContextHolder;
import com.xinra.nucleus.common.GenericContextHolder;
import com.xinra.reviewcommunity.rest.FrontendUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.xinra.reviewcommunity", "com.xinra.nucleus"})
public class ReviewCommunityApplication {
  
  public static void main(String[] args) {
    SpringApplication.run(ReviewCommunityApplication.class, args);
  }
  
  // generic type <?> is used to be able to override this bean to hold a subclass of Context
  @Bean
  public ContextHolder<?> contextHolder() {
    return new GenericContextHolder<>(Context.class, Context::new, FrontendUtil::isRequest);
  }
}
