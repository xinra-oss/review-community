package com.xinra.reviewcommunity.repo;

import com.xinra.reviewcommunity.entity.Product;
import com.xinra.reviewcommunity.entity.Review;
import com.xinra.reviewcommunity.entity.ReviewVote;
import com.xinra.reviewcommunity.shared.OrderBy;
import java.util.List;

public interface ReviewRepositoryCustom {
  
  /**
   * Finds all reviews of the given product.
   * 
   * @return
   *     List of {@link Review}s if no user is authenticated, otherwise list of arrays:<br>
   *     [0] => {@link Review}<br>
   *     [1] => {@link ReviewVote}
   */
  List<?> findByProduct(Product product, OrderBy orderBy);

}
