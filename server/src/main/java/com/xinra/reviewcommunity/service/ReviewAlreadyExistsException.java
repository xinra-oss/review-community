package com.xinra.reviewcommunity.service;

import com.xinra.reviewcommunity.entity.SerialEntity;

public class ReviewAlreadyExistsException extends RuntimeException {


  private static final long serialVersionUID = 1L;

  /**
   * Thrown if Review for a Product by a specific User already exists.
   */
  public ReviewAlreadyExistsException(Class<? extends SerialEntity> type,
                                      int serial, String userName) {
    super("The Entity with name " + type.getSimpleName()
            + " and serial " + serial
            + " already exists for User " + userName);
  }

}
