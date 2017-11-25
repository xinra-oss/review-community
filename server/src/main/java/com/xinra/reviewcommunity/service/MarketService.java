package com.xinra.reviewcommunity.service;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Streams;
import com.xinra.reviewcommunity.entity.Market;
import com.xinra.reviewcommunity.repo.MarketRepository;
import com.xinra.reviewcommunity.shared.dto.MarketDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
public class MarketService extends AbstractService implements 
    ApplicationListener<ContextRefreshedEvent>, Ordered {
  
  public static class MarketNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private MarketNotFoundException(String slug) {
      super("There is no market with slug '" + slug + "'");
    }
  }
  
  private @Autowired MarketRepository<Market> marketRepo;
  
  private MarketDto toDto(Market market) {
    MarketDto marketDto = dtoFactory.createDto(MarketDto.class);
    marketDto.setSlug(market.getSlug());
    marketDto.setName(market.getName());
    return marketDto;
  }

  /**
   * Returns the market with the given slug.
   * @throws MarketNotFoundException if there is no market with the given slug
   */
  public MarketDto getBySlug(String slug) {
    Market market = marketRepo.findBySlug(slug);
    if (market == null) {
      throw new MarketNotFoundException(slug);
    }
    return toDto(market);
  }
  
  /**
   * Returns a collection of all markets.
   */
  public ImmutableCollection<MarketDto> getAllMarkets() {
    return Streams.stream(marketRepo.findAllCached())
      .map(this::toDto)
      .collect(ImmutableList.toImmutableList());
  }
  
  /**
   * Creates a new market.
   */
  public void createMarket(MarketDto marketDto) {
    // TODO ensure that slug is unique!
    Market market = entityFactory.createEntity(Market.class);
    market.setName(marketDto.getName());
    market.setSlug(marketDto.getSlug());
    marketRepo.save(market);
    
    log.info("Created market with slug '{}'", marketDto.getSlug());
  }

  @Override
  public void onApplicationEvent(ContextRefreshedEvent event) {
    log.info("Start building market cache");
    // todo
    log.info("Finished building market cache");
  }

  @Override
  public int getOrder() {
    return 10;
  }
  
}
