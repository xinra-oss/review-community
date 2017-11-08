package com.xinra.reviewcommunity.service;

import com.xinra.reviewcommunity.shared.dto.DtoFactory;
import com.xinra.reviewcommunity.shared.dto.InstantiatingDtoFactory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@Configuration
@EnableSpringDataWebSupport
public class ServiceConfiguration {

  /**
   * For now, no interfaces are used and classes are instantiated directly.
   */
  @Bean
  public DtoFactory dtoFactory() {
    return new InstantiatingDtoFactory();
  }
  
}
