package com.xinra.reviewcommunity.service;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableMap;
import com.xinra.reviewcommunity.entity.Market;
import com.xinra.reviewcommunity.repo.MarketRepository;
import com.xinra.reviewcommunity.shared.dto.MarketDto;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
public class MarketService extends AbstractService 
    implements ApplicationListener<ContextRefreshedEvent>, Ordered {
  
  private @Autowired MarketRepository<Market> marketRepo;
  private @PersistenceContext EntityManager entityManager;
  
  private ReentrantReadWriteLock cacheLock = new ReentrantReadWriteLock();
  private ImmutableMap<String, MarketDto> dtoCache;
  private ImmutableMap<String, Market> entityCache;
  
  private MarketDto toDto(Market market) {
    MarketDto marketDto = dtoFactory.createDto(MarketDto.class);
    marketDto.setSlug(market.getSlug());
    return marketDto;
  }

  /**
   * Returns the market with the given slug.
   */
  public MarketDto getBySlug(String slug) {
    cacheLock.readLock().lock();
    MarketDto marketDto = dtoCache.get(slug);
    cacheLock.readLock().unlock();
    return marketDto;
  }
  
  /**
   * Returns the detached entity of the market with the given slug.
   */
  public Market getEntityBySlug(String slug) {
    cacheLock.readLock().lock();
    Market market = entityCache.get(slug);
    cacheLock.readLock().unlock();
    return market;
  }
  
  /**
   * Returns a collection of all markets.
   */
  public ImmutableCollection<MarketDto> getAllMarkets() {
    cacheLock.readLock().lock();
    ImmutableCollection<MarketDto> allMarketDtos = dtoCache.values();
    cacheLock.readLock().unlock();
    return allMarketDtos;
  }
  
  /**
   * Creates a new market.
   */
  public void createMarket(MarketDto marketDto) {
    cacheLock.writeLock().lock();
    try  {
      // TODO ensure that slug is unique!
      Market market = entityFactory.createEntity(Market.class);
      market.setName(marketDto.getName());
      market.setSlug(marketDto.getSlug());
      
      ServiceUtil.afterCommit(() -> {
        ImmutableMap.Builder<String, MarketDto> dtoCacheBuilder = ImmutableMap.builder();
        dtoCacheBuilder.putAll(dtoCache);
        dtoCacheBuilder.put(market.getSlug(), marketDto);
        dtoCache = dtoCacheBuilder.build();
        ImmutableMap.Builder<String, Market> entityCacheBuilder = ImmutableMap.builder();
        entityCacheBuilder.putAll(entityCache);
        entityCacheBuilder.put(market.getSlug(), market);
        entityCache = entityCacheBuilder.build();
        log.info("Created market with slug '{}'", market.getSlug());
      });
      
      marketRepo.save(market); // we create a new entity -> safely ignore the returned object
      // a detached entity would not be persisted, see https://stackoverflow.com/a/14329509/5519485
      entityManager.flush();
      entityManager.detach(market);
    } finally {
      cacheLock.writeLock().unlock();
    }
  }

  @Override
  public void onApplicationEvent(ContextRefreshedEvent event) {
    log.info("Start building market cache");
    ImmutableMap.Builder<String, MarketDto> dtoCacheBuilder = ImmutableMap.builder();
    ImmutableMap.Builder<String, Market> entityCacheBuilder = ImmutableMap.builder();
    
    marketRepo.findAll().forEach(market -> {
      entityManager.detach(market);
      entityCacheBuilder.put(market.getSlug(), market);
      dtoCacheBuilder.put(market.getSlug(), toDto(market));
    });
    
    dtoCache = dtoCacheBuilder.build();
    entityCache = entityCacheBuilder.build();
    log.info("Finished building market cache. {} markets loaded", dtoCache.size());
  }

  @Override
  public int getOrder() {
    return 10;
  }
  
}
