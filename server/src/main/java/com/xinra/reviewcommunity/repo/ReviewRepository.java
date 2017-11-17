package com.xinra.reviewcommunity.repo;

import com.xinra.nucleus.entity.AbstractEntityRepository;
import com.xinra.reviewcommunity.entity.Review;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository<T extends Review> extends AbstractEntityRepository<T>,
    ReviewRepositoryCustom {

  T findBySerialAndProductSerial(int serial, int productSerial);

  T findByUserIdAndProductId(String userId, String productId);
  
}
