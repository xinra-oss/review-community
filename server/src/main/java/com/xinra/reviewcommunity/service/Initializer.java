package com.xinra.reviewcommunity.service;

import com.xinra.nucleus.service.ServiceProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * Runs tasks that must be executed once during application startup.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class Initializer implements ApplicationListener<ContextRefreshedEvent> {
  
  private final ServiceProvider serviceProvider;

  @Override
  public void onApplicationEvent(ContextRefreshedEvent event) {
    
    serviceProvider.getService(SearchService.class).rebuildIndex();
    
    log.info("Starting initial cache buildiung");
    serviceProvider.getService(MarketService.class).buildCache();
    log.info("Finished initial cache buildiung");
  }

}
