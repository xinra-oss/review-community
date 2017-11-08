package com.xinra.reviewcommunity.shared.dto;

/**
 * Creates an instance of the DTO class.
 */
public class InstantiatingDtoFactory implements DtoFactory {
  @Override
  public <T extends Dto> T createDto(Class<T> type) {
    try {
      return type.newInstance();
    } catch (InstantiationException | IllegalAccessException ex) {
      throw new RuntimeException(ex);
    }
  }
}
