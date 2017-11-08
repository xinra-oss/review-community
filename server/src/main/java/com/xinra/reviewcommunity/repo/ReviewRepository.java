package com.xinra.reviewcommunity.repo;

import com.xinra.reviewcommunity.entity.Review;
import java.util.Set;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository<T extends Review> extends SerialEntityRepository<T> {

  Set<T> findByProductId(String productId);

  Set<T> findByProductIdOrderByCreatedAtDesc(String productId);

  Set<T> findByProductIdOrderByRatingDesc(String productId);

  T findByUserIdAndProductId(String userId, String productId);
}
