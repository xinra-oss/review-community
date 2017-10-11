package com.xinra.reviewcommunity.service;

import com.xinra.nucleus.entity.EntityPk;
import com.xinra.reviewcommunity.dto.MarketDto;
import com.xinra.reviewcommunity.entity.Market;
import com.xinra.reviewcommunity.repo.MarketRepository;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
public class MarketService extends AbstractService {
  
  @Autowired
  private MarketRepository<Market> marketRepo;
  
  @PersistenceContext
  private EntityManager entityManager;
  
  private Map<String, MarketDto> dtoCache;
  private Map<EntityPk, Market> entityCache;
  
  /**
   * Loads market information from database into cache. 
   */
  public void buildCache() {
    log.info("Building market cache");
    dtoCache = new HashMap<>();
    entityCache = new HashMap<>();
    
    marketRepo.findAll().forEach(market -> {
      entityManager.detach(market);
      entityCache.put(market.getPk(), market);
      dtoCache.put(market.getSlug(), toDto(market));
    });
  }
  
  private MarketDto toDto(Market market) {
    MarketDto marketDto = dtoFactory.createDto(MarketDto.class);
    marketDto.setPk(market.getPk());
    marketDto.setSlug(market.getSlug());
    return marketDto;
  }

  public MarketDto getBySlug(String slug) {
    return dtoCache.get(slug);
  }
  
  Market getEntity(EntityPk pk) {
    return entityCache.get(pk);
  }
  
}
