package com.xinra.reviewcommunity.repo;

import com.xinra.reviewcommunity.entity.Brand;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandRepository<T extends Brand> extends SerialEntityRepository<T> {

}
