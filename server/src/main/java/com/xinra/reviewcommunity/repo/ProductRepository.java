package com.xinra.reviewcommunity.repo;

import com.xinra.reviewcommunity.entity.Product;
import java.util.Set;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository<T extends Product> extends SerialEntityRepository<T> {

  Set<T> findProductsByBrandSerial(int serial);
  Set<T> findProductsByCategorySerial(int serial);
}
