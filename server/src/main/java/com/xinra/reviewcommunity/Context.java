package com.xinra.reviewcommunity;

import com.xinra.reviewcommunity.dto.MarketDto;
import java.util.Optional;
import lombok.Getter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

/**
 * Holds data that is specific to a request and is needed across the application
 * so that it doesn't need to be passed as method parameters in many places.
 */
@Component
@RequestScope
public class Context {
  
  //this will be set once and read many times -> cache Optional
  private @Getter Optional<MarketDto> market = Optional.empty();
  
  /**
   * Sets the current market. May be {@code null} to indicate a market-agnostic context.
   */
  public void setMarket(MarketDto market) {
    this.market = Optional.ofNullable(market);
  }
  
}
