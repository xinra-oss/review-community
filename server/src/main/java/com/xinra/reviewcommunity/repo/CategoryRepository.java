package com.xinra.reviewcommunity.repo;

import com.xinra.nucleus.entity.AbstractEntityRepository;
import com.xinra.reviewcommunity.entity.Category;
import com.xinra.reviewcommunity.entity.Market;
import java.util.Collection;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository<T extends Category> extends AbstractEntityRepository<T> {

  @CacheQuery
  T findBySerial(int serial);
  
  @CacheQuery
  @Query("SELECT c, (SELECT COUNT(p) FROM c.products p WHERE p.market = :market) FROM Category c")
  Collection<Object[]> findAllWithNumProducts(@Param("market") Market market);
  
}
