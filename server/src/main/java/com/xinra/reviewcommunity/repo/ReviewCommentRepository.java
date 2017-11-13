package com.xinra.reviewcommunity.repo;

import com.xinra.reviewcommunity.entity.ReviewComment;
import java.util.Set;

public interface ReviewCommentRepository<T extends ReviewComment>
        extends com.xinra.nucleus.entity.AbstractEntityRepository<T> {

  Set<T> findByReviewIdOrderByCreatedAtAsc(String reviewId);

  T findBySerialAndReviewSerial(int serial, int reviewSerial);
}
