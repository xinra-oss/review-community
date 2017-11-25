package com.xinra.reviewcommunity.entity;

import com.xinra.nucleus.common.ContextHolder;
import com.xinra.reviewcommunity.Context;
import com.xinra.reviewcommunity.repo.MarketRepository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Automatically enables filters on database requests that are
 * dependent on the context.
 */
@Aspect
@Component
@Transactional
public class ContextAwareFilterAdvice {
  
  private @PersistenceContext EntityManager entityManager;
  private @Autowired ContextHolder<Context> contextHolder;
  private @Autowired MarketRepository<Market> marketRepo;
  
  /**
   * Enables context-specific filters for all repository methods.
   */
  @Before("this(org.springframework.data.repository.Repository)")
  public void enableFilters(JoinPoint joinPoint) {
    
    if (!entityManager.isJoinedToTransaction()) {
      return; // this is not an actual query
    }
    
    if (joinPoint.getTarget() instanceof MarketRepository) {
      return; // would cause endless recursion
    }
    
    final Session session = entityManager.unwrap(Session.class);
    final Context context;
    
    try {
      context = contextHolder.get();
    } catch (IllegalStateException ex) {
      // If this is not a web request and no mock context has been set, this is
      // *probably* on purpose (only context-agnostic queries are executed).
      // In this case, disable all filters.
      session.disableFilter("market");
      return;
    }
      
    if (context.getMarket().isPresent()) {
      String marketId = marketRepo.findBySlug(context.getMarket().get().getSlug())
          .getPk().getId();
      session.enableFilter("market").setParameter("marketId", marketId);
    } else {
      session.disableFilter("market");
    }
  }

}
