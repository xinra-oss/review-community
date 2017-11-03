package com.xinra.reviewcommunity.service;

import com.xinra.reviewcommunity.entity.SerialEntity;

/**
 * Thrown by services if no entity with the given serial exists.
 */
public class SerialNotFoundException extends RuntimeException {
  
  private static final long serialVersionUID = 1L;
  
  public SerialNotFoundException(Class<? extends SerialEntity> type, int serial) {
    super("There is no entity of type " + type.getSimpleName() + " with serial " + serial);
  }

}
