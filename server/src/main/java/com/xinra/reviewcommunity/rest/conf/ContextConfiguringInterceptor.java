package com.xinra.reviewcommunity.rest.conf;

import com.xinra.nucleus.service.ServiceProvider;
import com.xinra.reviewcommunity.Context;
import com.xinra.reviewcommunity.dto.MarketDto;
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
  
  @SuppressWarnings("unused")
  private static class MarketNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    
    public MarketNotFoundException(String slug) {
      super("Market with slug '" + slug + "' does not exist");
    }
  }
  
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
      if (market == null) {
        response.sendError(404);
        return false;
        // TODO throw new MarketNotFoundException(slug);
      }
      context.setMarket(market);
    } // else handler is @MarketAgnostic
  
    return true;
  }
  
  @SuppressWarnings("unchecked")
  private Map<String, String> getPathParameters(HttpServletRequest request) {
    return (Map<String, String>) request
        .getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
  }
  
}
