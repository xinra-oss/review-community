package com.xinra.reviewcommunity.repo;

import com.xinra.nucleus.entity.AbstractEntityRepository;
import com.xinra.reviewcommunity.entity.ReviewVote;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewVoteRepository<T extends ReviewVote> extends AbstractEntityRepository<T> {

  T findByUserIdAndReviewId(String userId, String reviewId);
}
