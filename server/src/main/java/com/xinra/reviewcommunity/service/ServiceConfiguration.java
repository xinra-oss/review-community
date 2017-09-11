package com.xinra.reviewcommunity.service;

import com.xinra.nucleus.service.Dto;
import com.xinra.nucleus.service.DtoFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfiguration {

  /**
   * For now, no interfaces are used and classes are instantiated directly.
   */
  @Bean
  public DtoFactory dtoFactory() {
    return new DtoFactory() {
      @Override public <T extends Dto> T createDto(Class<T> type) {
        try {
          return type.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
          throw new RuntimeException(ex);
        }
      }
    };
  }
  
}
