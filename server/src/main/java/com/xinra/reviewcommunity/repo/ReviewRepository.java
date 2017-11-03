package com.xinra.reviewcommunity.repo;

import com.xinra.reviewcommunity.entity.Review;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository<T extends Review> extends SerialEntityRepository<T> {

}
