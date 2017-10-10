package com.xinra.reviewcommunity.service;

import com.xinra.nucleus.service.ServiceProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CacheBuilder implements ApplicationListener<ContextRefreshedEvent> {
  
  private final ServiceProvider serviceProvider;

  @Override
  public void onApplicationEvent(ContextRefreshedEvent event) {
    log.info("Starting initial cache buildiung");
    serviceProvider.getService(MarketService.class).buildCache();
  }

}
