package com.xinra.reviewcommunity;

import com.xinra.reviewcommunity.dto.AuthenticatedUserDto;
import com.xinra.reviewcommunity.shared.dto.MarketDto;
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
  
  //these will be set once and read many times -> Optional is cached
  
  private @Getter Optional<MarketDto> market = Optional.empty();
  private @Getter Optional<AuthenticatedUserDto> authenticatedUser = Optional.empty();
  
  /**
   * Sets the current market. May be {@code null} to indicate a market-agnostic context (this is the
   * default).
   */
  public void setMarket(MarketDto market) {
    this.market = Optional.ofNullable(market);
  }
  
  /**
   * Sets the currently authenticated user. May be {@code null} to indicate that no user is
   * authenticated (this is the default).
   */
  public void setAuthenticatedUser(AuthenticatedUserDto authenticatedUser) {
    this.authenticatedUser = Optional.ofNullable(authenticatedUser);
  }
  
}
