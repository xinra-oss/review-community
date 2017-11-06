package com.xinra.reviewcommunity.android;

import com.xinra.reviewcommunity.shared.Permission;
import com.xinra.reviewcommunity.shared.dto.CategoryDto;
import com.xinra.reviewcommunity.shared.dto.CsrfTokenDto;
import com.xinra.reviewcommunity.shared.dto.MarketDto;
import com.xinra.reviewcommunity.shared.dto.UserDto;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

/**
 * This is the global application state. Everything that is not local UI state is managed here.
 * {@link BehaviorSubject}s are used so that the current state is pushed to new subscribers. Make
 * sure to properly dispose of subscriptions as to not leak them.
 */
public class AppState {

  public final BehaviorSubject<Boolean> initialized = BehaviorSubject.createDefault(false);

  // For these we don't need to notify observers of updates.
  public CsrfTokenDto csrfToken;
  public String sessionId;
  public final BehaviorSubject<Optional<UserDto>> authenticatedUser = BehaviorSubject.create();
  public final BehaviorSubject<Set<Permission>> permissions = BehaviorSubject.create();

  public final BehaviorSubject<MarketDto> market = BehaviorSubject.create();
  public final BehaviorSubject<List<MarketDto>> availableMarkets = BehaviorSubject.create();
  public final BehaviorSubject<List<CategoryDto>> categoryTree = BehaviorSubject.create();

  /**
   * Determines if the user is signed in.
   */
  public Observable<Boolean> isAuthenticated() {
    return authenticatedUser.map(Optional::isPresent);
  }

  /**
   * Determines if the given permission is granted to the current user.
   */
  public Observable<Boolean> hasPermission(Permission permission) {
    return permissions.map(permissions -> permissions.contains(permission));
  }

}
