package com.xinra.reviewcommunity.rest.conf;

import com.xinra.reviewcommunity.MultiMarketMode;
import com.xinra.reviewcommunity.rest.MarketAgnostic;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * Modifies controller request mappings. If multi market mode is enabled and market should be
 * resolved by path, this adds the path variable {market} to all handlers that are not marked
 * as {@link MarketAgnostic}. If enabled, a constant prefix is added to all REST mappings.
 */
public class PrefixingRequestMappingHandlerMapping extends RequestMappingHandlerMapping {
  
  private final MultiMarketMode multiMarketMode;

  public PrefixingRequestMappingHandlerMapping(MultiMarketMode multiMarketMode) {
    this.multiMarketMode = multiMarketMode;
  }

  @Override
  protected RequestMappingInfo getMappingForMethod(Method method, Class<?> handlerType) {
    RequestMappingInfo info = super.getMappingForMethod(method, handlerType);
    
    if (info == null 
        || !handlerType.getPackage().getName().startsWith("com.xinra.reviewcommunity")) {
      return info;
    }
    
    String marketPrefix = multiMarketMode == MultiMarketMode.PATH
        && (AnnotatedElementUtils.hasAnnotation(method, MarketAgnostic.class)
        || AnnotatedElementUtils.hasAnnotation(handlerType, MarketAgnostic.class))
        ? "" : "/{market}";
    
    // TODO make api prefix configurable
    String apiPrefix = AnnotatedElementUtils.hasAnnotation(method, ResponseBody.class)
        || AnnotatedElementUtils.hasAnnotation(handlerType, ResponseBody.class)
        ? "/api" : "";
    
    Set<String> patterns = info.getPatternsCondition().getPatterns().stream()
        .map(p -> marketPrefix + apiPrefix + p)
        .collect(Collectors.toSet());
    
    try {
      Field field = info.getPatternsCondition().getClass().getDeclaredField("patterns");
      field.setAccessible(true);
      field.set(info.getPatternsCondition(), patterns);
    } catch (NoSuchFieldException | SecurityException | IllegalArgumentException 
        | IllegalAccessException ex) {
      throw new RuntimeException(ex);
    }
    
    return info;
  }
  
}
