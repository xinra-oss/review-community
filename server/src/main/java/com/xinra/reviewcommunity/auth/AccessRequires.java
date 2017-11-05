package com.xinra.reviewcommunity.auth;

import com.xinra.reviewcommunity.shared.Permission;
import com.xinra.reviewcommunity.shared.Role;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.core.annotation.AliasFor;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.access.annotation.AnnotationMetadataExtractor;
import org.springframework.security.access.annotation.Secured;

/**
 * This annotation is used in place of {@link Secured} with type-safe {@link Permission}s and
 * {@link Role}s.
 * 
 * @see PermissionVoter
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface AccessRequires {
  
  /**
   * Alias for {@link #permissions()}.
   */
  @AliasFor("permissions")
  public Permission[] value() default {};
  
  /**
   * If the authenticated user has at least one of these permissions, access is granted.
   */
  @AliasFor("value")
  public Permission[] permissions() default {};
  
  /**
   * If the authenticated user has at least one of these roles, access is granted.
   */
  public Role[] roles() default {};
  
  public static class MetadataExtractor implements AnnotationMetadataExtractor<AccessRequires> {

    @Override
    public Collection<? extends ConfigAttribute> 
        extractAttributes(AccessRequires securityAnnotation) {
      
      Permission[] permissions = securityAnnotation.permissions();
      Role[] roles = securityAnnotation.roles();
      List<ConfigAttribute> attributes = new ArrayList<>(permissions.length + roles.length);

      for (Permission permission : permissions) {
        attributes.add(new SecurityConfig(permission.toString()));
      }
      for (Role role : roles) {
        attributes.add(new SecurityConfig(role.toString()));
      }

      return attributes;
    }
  }
}
