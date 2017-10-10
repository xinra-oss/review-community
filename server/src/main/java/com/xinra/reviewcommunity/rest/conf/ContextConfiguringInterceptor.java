package com.xinra.reviewcommunity.rest.conf;

import com.xinra.nucleus.service.ServiceProvider;
import com.xinra.reviewcommunity.Context;
import com.xinra.reviewcommunity.MultiMarketMode;
import com.xinra.reviewcommunity.service.MarketDto;
import com.xinra.reviewcommunity.service.MarketService;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
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
  
  @Autowired
  private MultiMarketMode multiMarketMode;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {

    switch (multiMarketMode) {
      case PATH:
        String slug = getPathParameters(request).get("market");
        if (slug == null) {
          break; // handler is @MarketAgnostic
        }
        MarketDto market = serviceProvider.getService(MarketService.class).getBySlug(slug);
        if (market == null) {
          // TODO throw exception
          response.sendError(404);
          return false;
        }
        context.setMarket(market);
        break;
      case SUBDOMAIN:
        // TODO implement market distinction by subdomain
        break;
      default: // DISABLED
    }
    
    return true;
    
  }
  
  @SuppressWarnings("unchecked")
  private Map<String, String> getPathParameters(HttpServletRequest request) {
    return (Map<String, String>) request
        .getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
  }
  
}
