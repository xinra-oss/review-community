package com.xinra.reviewcommunity.repo;

import com.xinra.nucleus.entity.AbstractEntityRepository;
import com.xinra.reviewcommunity.entity.Product;
import java.util.Collection;
import java.util.Set;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository<T extends Product> extends AbstractEntityRepository<T> {

  T findBySerial(int serial);

  Set<T> findByBrandSerial(int serial);

  @Query("SELECT p FROM Product p JOIN FETCH p.brand WHERE p.category.id IN (:categoryIds)")
  Set<T> findByCategoryIds(@Param("categoryIds") Collection<String> categoryIds);

}
