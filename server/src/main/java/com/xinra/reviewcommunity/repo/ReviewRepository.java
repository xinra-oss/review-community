package com.xinra.reviewcommunity.repo;

import com.xinra.reviewcommunity.entity.Review;
import java.util.Set;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository<T extends Review> extends com.xinra.nucleus.entity.AbstractEntityRepository<T> {

  Set<T> findByProductId(String productId);

  T findBySerial(int serial);

  T findBySerialAndProductSerial(int serial, int productSerial);

  Set<T> findByProductIdOrderByCreatedAtDesc(String productId);

  Set<T> findByProductIdOrderByRatingDesc(String productId);

  T findByUserIdAndProductId(String userId, String productId);
}
