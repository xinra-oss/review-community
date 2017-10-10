package com.xinra.reviewcommunity;

import com.xinra.nucleus.common.ContextHolder;
import com.xinra.reviewcommunity.rest.conf.MarketAgnostic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Profile("test")
@RequestMapping("/test")
public class SampleController {
  
  private @Autowired ContextHolder<Context> contextHolder;
  private @Autowired MultiMarketMode multiMarketMode;
  
  /** Market-aware regular controller. Returns current market slug. */
  @RequestMapping("/marketAware")
  public ResponseEntity<String> marketAware() {
    return new ResponseEntity<>(
        multiMarketMode.isEnabled() ? contextHolder.get().getMarket().get().getSlug() : "none",
        HttpStatus.OK);
  }
  
  /** Market-agnostic regular controller. */
  @MarketAgnostic
  @RequestMapping("/marketAgnostic")
  public ResponseEntity<String> marketAgnostic() {
    return new ResponseEntity<>(HttpStatus.OK);
  }
  
  /** Market-aware REST controller. Returns current market slug. */
  @ResponseBody
  @RequestMapping("/marketAwareRest")
  public String marketAwareRest() {
    return multiMarketMode.isEnabled() ? contextHolder.get().getMarket().get().getSlug() : "none";
  }
}
