package com.xinra.reviewcommunity.entity;

import com.xinra.nucleus.common.ApplicationContextProvider;
import com.xinra.nucleus.entity.BaseEntity;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

public interface EntityUpdateListener<T extends BaseEntity> {
  
  /**
   * Retrieves a bean of the given type from the application context and calls it if there is one.
   */
  static <T extends BaseEntity> void delegateToBean(
      Class<? extends EntityUpdateListener<T>> beanType, T entity) {
    
    try {
      ApplicationContextProvider.getApplicationContext().getBean(beanType).onEntityUpdate(entity);
    } catch (NoSuchBeanDefinitionException ex) {
      // ignore
    }
  }
  
  void onEntityUpdate(T entity);
}