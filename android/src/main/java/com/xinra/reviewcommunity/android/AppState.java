package com.xinra.reviewcommunity.android;

import com.google.common.collect.ImmutableSet;
import com.xinra.reviewcommunity.shared.Permission;
import com.xinra.reviewcommunity.shared.dto.MarketDto;
import com.xinra.reviewcommunity.shared.dto.UserDto;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

/**
 * This is the global application state. Everything that is not local UI state is managed here.
 * {@link BehaviorSubject}s are used so that the current state is pushed to new subscribers. Make
 * sure to properly dispose of subscriptions as to not leak them.
 */
public class AppState {

  // For these we don't need to notify observers of updates.
  public String csrfToken;
  public String sessionId;

  public final BehaviorSubject<UserDto> authenticatedUser = BehaviorSubject.create();
  public final BehaviorSubject<ImmutableSet<Permission>> permissions = BehaviorSubject.create();

  public final BehaviorSubject<MarketDto> market = BehaviorSubject.create();

  /**
   * Determines if the user is signed in.
   */
  public Observable<Boolean> isAuthenticated() {
    return authenticatedUser.map(authenticatedUser -> authenticatedUser != null);
  }

  /**
   * Determines if the given permission is granted to the current user.
   */
  public Observable<Boolean> hasPermission(Permission permission) {
    return permissions.map(permissions -> permissions.contains(permission));
  }

}
