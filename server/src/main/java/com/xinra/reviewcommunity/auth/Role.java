package com.xinra.reviewcommunity.auth;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.xinra.reviewcommunity.entity.UserLevel;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * A role is a set of {@link Permission}s. Permissions can be inherited from one or more roles.
 * Each user can have multiple roles.
 * 
 * @see UserLevel
 */
@Getter
@RequiredArgsConstructor
public enum Role {
  
  // New roles can be added here.
  
  /**
   * Default role of newly registered users.
   */
  USER(
    inheritsFrom(),
    addsPermissions(
      Permission.ADD_PRODUCT,
      Permission.ADD_REVIEW
    )
  ),
  MODERATOR(
    inheritsFrom(USER),
    addsPermissions(
      Permission.DELETE_PRODUCT
    )
  ),
  ADMIN(
    inheritsFrom(MODERATOR),
    addsPermissions(
      
    )
  );

  private static ImmutableSet<Role> inheritsFrom(Role... parents) {
    return ImmutableSet.copyOf(parents);
  }
  
  private static ImmutableSet<Permission> addsPermissions(Permission... permissions) {
    return ImmutableSet.copyOf(permissions);
  }
  
  /**
   * Returns all the transitive roles (see {@link #getTransitiveRoles()}) of a collection of roles
   * combined into one enum set.
   */
  public static ImmutableSet<Role> getAllTransitiveRoles(@NonNull Collection<Role> roles) {
    return roles.stream()
        .map(Role::getTransitiveRoles)
        .flatMap(Set::stream)
        .collect(Sets.toImmutableEnumSet());
  }
  
  /**
   * Returns all the transitive permissions (see {@link #getTransitivePermissions()}) of a
   * collection of roles combined into one enum set.
   */
  public static ImmutableSet<Permission> 
      getAllTransitivePermissions(@NonNull Collection<Role> roles) {
    return roles.stream()
        .map(Role::getTransitivePermissions)
        .flatMap(Set::stream)
        .collect(Sets.toImmutableEnumSet());
  }
  
  private final ImmutableSet<Role> parents;
  private final ImmutableSet<Permission> permissions;
  private ImmutableSet<Role> transitiveRoles;
  private ImmutableSet<Permission> transitivePermissions;
  
  /**
   * Returns an enum set of this role as well of all roles that are inherited.
   */
  public ImmutableSet<Role> getTransitiveRoles() {
    if (transitiveRoles == null) {
      Set<Role> roles = new HashSet<>();
      roles.add(this);
      // it is not possible to declare circular inheritance
      parents.forEach(p -> roles.addAll(p.getTransitiveRoles()));
      transitiveRoles = Sets.immutableEnumSet(roles);
    }
    return transitiveRoles;
  }
  
  /**
   * Returns an enum set of all permissions that are granted to this role.
   * This includes inherited permissions.
   */
  public ImmutableSet<Permission> getTransitivePermissions() {
    if (transitivePermissions == null) {
      Set<Permission> roles = new HashSet<>();
      roles.addAll(permissions);
      parents.forEach(p -> roles.addAll(p.getTransitivePermissions()));
      transitivePermissions = Sets.immutableEnumSet(roles);
    }
    return transitivePermissions;
  }
  
  /**
   * Prefixes constant names with "ROLE_".
   */
  @Override
  public String toString() {
    return "ROLE_" + name();
  }
  
}
