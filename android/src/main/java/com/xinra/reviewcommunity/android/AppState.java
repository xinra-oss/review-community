package com.xinra.reviewcommunity.android;

import com.xinra.reviewcommunity.shared.Permission;
import com.xinra.reviewcommunity.shared.dto.CategoryDto;
import com.xinra.reviewcommunity.shared.dto.CsrfTokenDto;
import com.xinra.reviewcommunity.shared.dto.MarketDto;
import com.xinra.reviewcommunity.shared.dto.UserDto;

import java.util.HashMap;
import java.util.List;
import com.google.common.base.Optional;

import java.util.Map;
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
  public String sessionCookie;
  public final BehaviorSubject<Optional<UserDto>> authenticatedUser = BehaviorSubject.createDefault(Optional.absent());
  public final BehaviorSubject<Set<Permission>> permissions = BehaviorSubject.create();

  public final BehaviorSubject<MarketDto> market = BehaviorSubject.create();
  public final BehaviorSubject<List<MarketDto>> availableMarkets = BehaviorSubject.create();
  public final BehaviorSubject<List<CategoryDto>> categoryTree = BehaviorSubject.create();
  /** Maps category serial to DTO. */
  public final Observable<Map<Integer, CategoryDto>> categoryMap = categoryTree.map(this::buildCategoryMap);

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

  private Map<Integer, CategoryDto> buildCategoryMap(List<CategoryDto> categoryTree) {
    Map<Integer, CategoryDto> categoryMap = new HashMap<>();
    addToCategoryMapRecursively(categoryTree, categoryMap);
    return  categoryMap;
  }

  private void addToCategoryMapRecursively(List<CategoryDto> categorySubTree, Map<Integer, CategoryDto> categoryMap) {
    for (CategoryDto category: categorySubTree) {
      categoryMap.put(category.getSerial(), category);
      addToCategoryMapRecursively(category.getChildren(), categoryMap);
    }
  }

}
