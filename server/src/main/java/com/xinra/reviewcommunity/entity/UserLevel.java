package com.xinra.reviewcommunity.entity;

import java.util.Set;
import lombok.Getter;
import lombok.NonNull;
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
  
  /**
   * Returns the level that is determined by a set of roles or {@code null} if the set doesn't
   * contain any {@link #associatedRole}.
   */
  public static UserLevel getFromRoles(@NonNull Set<Role> roles) {
    for (UserLevel level : UserLevel.values()) {
      if (roles.contains(level.associatedRole)) {
        return level;
      }
    }
    return null;
  }
  
  private final Role associatedRole;
  
}
