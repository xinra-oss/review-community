package com.xinra.reviewcommunity.repo;

import com.xinra.nucleus.common.ContextHolder;
import com.xinra.reviewcommunity.Context;
import com.xinra.reviewcommunity.dto.AuthenticatedUserDto;
import com.xinra.reviewcommunity.entity.Product;
import com.xinra.reviewcommunity.shared.OrderBy;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;

public class ReviewRepositoryImpl implements ReviewRepositoryCustom {
  
  private @PersistenceContext EntityManager entityManager;
  private @Autowired ContextHolder<Context> contextHolder;

  @Override
  public List<?> findByProduct(Product product, OrderBy orderBy) {
    
    final Optional<AuthenticatedUserDto> user = contextHolder.get().getAuthenticatedUser();
    
    String queryString = "SELECT r";
    
    if (user.isPresent()) {
      queryString += ", (SELECT v FROM ReviewVote v WHERE v.review.id = r.id AND v.user.id = :userId)";
    }
    
//    String queryString = user.isPresent()
//        ? "SELECT r, v FROM Review r, ReviewVote v LEFT JOIN ReviewVote WHERE v.user.id = :userId "
//        : "SELECT r FROM Review r WHERE 1=1 ";
    
    queryString += " FROM Review r WHERE r.product = :product ORDER BY ";
    queryString += orderBy == OrderBy.DATE ? "r.createdAt " : "r.score ";
    queryString += "DESC";
    
    final Query query = entityManager.createQuery(queryString);
    query.setParameter("product", product);
    if (user.isPresent()) {
      query.setParameter("userId", user.get().getPk().getId());
    }
    
    return query.getResultList();
  }

}
