package com.xinra.reviewcommunity.repo;

import com.xinra.reviewcommunity.entity.ReviewComment;
import java.util.Set;

public interface ReviewCommentRepository<T extends ReviewComment>
        extends SerialEntityRepository<T> {

  Set<T> findByReviewIdOrderByCreatedAtAsc(String reviewId);
}
