package com.xinra.reviewcommunity.repo;

import com.xinra.nucleus.entity.AbstractEntityRepository;
import com.xinra.reviewcommunity.entity.Vote;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteRepository<T extends Vote> extends AbstractEntityRepository<T> {

  T findByUserIdAndReviewId(String userId, String reviewId);
}
