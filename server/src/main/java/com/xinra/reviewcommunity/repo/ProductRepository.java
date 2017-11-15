package com.xinra.reviewcommunity.repo;

import com.xinra.reviewcommunity.entity.Product;

import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ProductRepository<T extends Product> extends com.xinra.nucleus.entity.AbstractEntityRepository<T> {

  T findBySerial(int serial);

  Set<T> findProductsByBrandSerial(int serial);

  Set<T> findProductsByCategorySerial(int serial);

}
