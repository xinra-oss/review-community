package com.xinra.reviewcommunity.service;

import com.xinra.nucleus.common.ContextHolder;
import com.xinra.nucleus.entity.BaseEntity;
import com.xinra.nucleus.entity.DefaultEntityFactory;
import com.xinra.nucleus.service.ServiceProvider;
import com.xinra.reviewcommunity.Context;
import com.xinra.reviewcommunity.entity.MarketSpecificEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/**
 * Automatically sets the state of certain entities according to context.
 */
@Primary
@Component
public class ContextAwareEntityFactory extends DefaultEntityFactory {
  
  private @Autowired ServiceProvider serviceProvider;
  private @Autowired ContextHolder<Context> contextHolder;

  @Override
  public <T extends BaseEntity> T createEntity(Class<T> type) {
    T entity = super.createEntity(type);
    
    if (entity instanceof MarketSpecificEntity) {
      ((MarketSpecificEntity) entity).setMarket(serviceProvider.getService(MarketService.class)
          .getEntity(contextHolder.get().getMarket().get().getSlug()));
    }
    
    return entity;
  }

}
