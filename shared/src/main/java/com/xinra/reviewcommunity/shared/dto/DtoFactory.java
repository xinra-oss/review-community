package com.xinra.reviewcommunity.shared.dto;

/**
 * Used to create DTOs. We define our own interface here to avoid a dependency on nucleus-service.
 */
public interface DtoFactory {

  /**
   * Creates a DTO instance of the desired type.
   */
  <T extends Dto> T createDto(Class<T> type);

}
