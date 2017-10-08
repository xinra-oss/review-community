package com.xinra.reviewcommunity.auth;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

/**
 * Enables the use of {@link Permission}s as authorities in Spring Security. The default
 * {@link RoleVoter} only supports authorities prefixed with "ROLE_". That's how roles are
 * distinguished from other authorities. This implementation doesn't rely on a prefix but checks if
 * the authority is contained in the {@link Permission} enum.
 * 
 * <p>Voters are not used everywhere to check authorities. They are, however, used for method
 * security. This class is required for {@link AccessRequires} to work in place of
 * {@link Secured}.
 */
public class PermissionVoter implements AccessDecisionVoter<Object> {
  
  /**
   * This is a cache of string representations of all permissions. This way we can use
   * {@link Set#contains(Object)} instead of {@link Permission#valueOf(String)} in
   * {@link #supports(ConfigAttribute)}.
   */
  private final Set<String> permissions;
  
  /**
   * Creates a PermissionVoter.
   */
  public PermissionVoter() {
    permissions = new HashSet<>();
    for (Permission permission : Permission.values()) {
      permissions.add(permission.name());
    }
  }

  @Override
  public boolean supports(ConfigAttribute attribute) {
    return permissions.contains(attribute.getAttribute());
  }

  @Override
  public boolean supports(Class<?> clazz) {
    return true;
  }

  /**
   * {@inheritDoc}
   * @implSpec adapted from {@link RoleVoter}.
   */
  @Override
  public int vote(Authentication authentication, Object object,
      Collection<ConfigAttribute> attributes) {
    
    if (authentication == null) {
      return ACCESS_DENIED;
    }
    int result = ACCESS_ABSTAIN;
    Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

    for (ConfigAttribute attribute : attributes) {
      if (this.supports(attribute)) {
        result = ACCESS_DENIED;

        // Attempt to find a matching granted authority
        for (GrantedAuthority authority : authorities) {
          if (attribute.getAttribute().equals(authority.getAuthority())) {
            return ACCESS_GRANTED;
          }
        }
      }
    }

    return result;
  }

}
