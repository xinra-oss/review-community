package com.xinra.reviewcommunity.repo;

import com.xinra.nucleus.entity.AbstractEntityRepository;
import com.xinra.reviewcommunity.entity.Product;
import java.util.Set;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository<T extends Product> extends AbstractEntityRepository<T> {

  T findBySerial(int serial);

  Set<T> findByBrandSerial(int serial);

  Set<T> findByCategorySerial(int serial);

}
