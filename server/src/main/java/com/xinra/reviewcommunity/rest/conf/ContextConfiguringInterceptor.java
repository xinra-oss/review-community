package com.xinra.reviewcommunity.rest.conf;

import com.xinra.nucleus.service.ServiceProvider;
import com.xinra.reviewcommunity.Context;
import com.xinra.reviewcommunity.dto.AuthenticatedUserDto;
import com.xinra.reviewcommunity.service.MarketService;
import com.xinra.reviewcommunity.shared.dto.MarketDto;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * Request interceptor that sets up the {@link Context} before a request
 * is handled by the controller.
 */
@Component
public class ContextConfiguringInterceptor extends HandlerInterceptorAdapter {
  
  @Autowired
  private Context context;
  
  @Autowired
  private ServiceProvider serviceProvider;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {

    String slug = getPathParameters(request).get("market");
    if (slug != null) {
      MarketDto market = serviceProvider.getService(MarketService.class).getBySlug(slug);
      context.setMarket(market);
    } // else handler is @MarketAgnostic
    
    if (request.getUserPrincipal() != null) {
      context.setAuthenticatedUser((AuthenticatedUserDto) 
          ((Authentication) request.getUserPrincipal()).getPrincipal());
    }
  
    return true;
  }
  
  @SuppressWarnings("unchecked")
  private Map<String, String> getPathParameters(HttpServletRequest request) {
    return (Map<String, String>) request
        .getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
  }
  
}
