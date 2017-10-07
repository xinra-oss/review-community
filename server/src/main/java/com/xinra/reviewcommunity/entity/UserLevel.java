package com.xinra.reviewcommunity.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * The level of a user is an indicator of his/her status and is displayed publicly in the client. It
 * is determined by the first {@link #associatedRole} that a user has by the order of declaration in
 * this enum.
 */
@Getter
@RequiredArgsConstructor
public enum UserLevel {

  ADMIN(Role.ADMIN),
  MODERATOR(Role.MODERATOR),
  USER(Role.USER);
  
  private final Role associatedRole;
  
}
