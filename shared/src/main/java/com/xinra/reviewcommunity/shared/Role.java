package com.xinra.reviewcommunity.shared;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
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
      Permission.CREATE_PRODUCT,
      Permission.CREATE_REVIEW,
      Permission.CREATE_REVIEW_COMMENT,
      Permission.VOTE
    )
  ),
  MODERATOR(
    inheritsFrom(USER),
    addsPermissions(
      Permission.DELETE_PRODUCT,
      Permission.CREATE_CATEGORY,
      Permission.CREATE_BRAND,
      Permission.DELETE_REVIEW,
      Permission.DELETE_REVIEW_COMMENT
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
    Set<Role> transitiveRoles = new HashSet<>();
    for (Role role: roles) {
      transitiveRoles.addAll(role.getTransitiveRoles());
    }
    return Sets.immutableEnumSet(transitiveRoles);
  }
  
  /**
   * Returns all the transitive permissions (see {@link #getTransitivePermissions()}) of a
   * collection of roles combined into one enum set.
   */
  public static ImmutableSet<Permission> 
      getAllTransitivePermissions(@NonNull Collection<Role> roles) {

    Set<Permission> transitivePermissions = new HashSet<>();
    for (Role role: roles) {
      transitivePermissions.addAll(role.getTransitivePermissions());
    }
    return Sets.immutableEnumSet(transitivePermissions);
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
      Set<Role> transitiveRoles = new HashSet<>();
      transitiveRoles.add(this);
      // it is not possible to declare circular inheritance
      for (Role child: parents) {
        transitiveRoles.addAll(child.getTransitiveRoles());
      }
      this.transitiveRoles = Sets.immutableEnumSet(transitiveRoles);
    }
    return transitiveRoles;
  }
  
  /**
   * Returns an enum set of all permissions that are granted to this role.
   * This includes inherited permissions.
   */
  public ImmutableSet<Permission> getTransitivePermissions() {
    if (transitivePermissions == null) {
      Set<Permission> transitivePermissions = new HashSet<>();
      transitivePermissions.addAll(permissions);
      for (Role child: parents) {
        transitivePermissions.addAll(child.getTransitivePermissions());
      }
      this.transitivePermissions = Sets.immutableEnumSet(transitivePermissions);
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
