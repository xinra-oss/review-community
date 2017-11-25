package com.xinra.reviewcommunity.rest;

import com.xinra.reviewcommunity.rest.conf.MarketAgnostic;
import com.xinra.reviewcommunity.service.MarketService;
import com.xinra.reviewcommunity.shared.dto.MarketDto;
import java.util.Collection;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@MarketAgnostic
@RequestMapping("/market")
public class MarketController extends AbstractController {
  
  /**
   * GET all markets.
   */
  @RequestMapping(path = "", method = RequestMethod.GET)
  public Collection<MarketDto> getList() {
    return serviceProvider.getService(MarketService.class).getAllMarkets();
  }

}
