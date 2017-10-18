package com.xinra.reviewcommunity.repo;

import com.xinra.reviewcommunity.entity.Product;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository<T extends Product> extends SerialEntityRepository<T> {

}
