package com.xinra.reviewcommunity.frontend;

import com.xinra.reviewcommunity.MultiMarketMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Configuration
public class FrontendConfiguration extends WebMvcConfigurationSupport {
  
  @Autowired
  private ContextConfiguringInterceptor requestContextInterceptor;
  
  @Autowired
  private MultiMarketMode multiMarketMode;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(requestContextInterceptor);
  }
  
  @Override
  protected RequestMappingHandlerMapping createRequestMappingHandlerMapping() {
    return new PrefixingRequestMappingHandlerMapping(multiMarketMode);
  }
  
}
