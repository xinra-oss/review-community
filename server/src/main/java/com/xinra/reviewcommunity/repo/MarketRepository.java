package com.xinra.reviewcommunity.repo;

import com.xinra.nucleus.entity.AbstractEntityRepository;
import com.xinra.reviewcommunity.entity.Market;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MarketRepository<T extends Market> extends AbstractEntityRepository<T> {
  
  @CacheQuery
  @Query("FROM Market")
  Iterable<T> findAllCached();
  
  @CacheQuery
  T findBySlug(String slug);

}
