package com.xinra.reviewcommunity.rest.conf;

import com.xinra.reviewcommunity.entity.Permission;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.access.annotation.AnnotationMetadataExtractor;
import org.springframework.security.access.annotation.Secured;

/**
 * This annotation is used in place of {@link Secured} with type-safe {@link Permission}s.
 * 
 * @see PermissionVoter
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface RequiresPermission {
  
  /**
   * The list of permissions, see {@link Secured#value()}.
   */
  public Permission[] value();
  
  public static class MetadataExtractor implements AnnotationMetadataExtractor<RequiresPermission> {

    @Override
    public Collection<? extends ConfigAttribute> 
        extractAttributes(RequiresPermission securityAnnotation) {
      
      Permission[] permissions = securityAnnotation.value();
      List<ConfigAttribute> attributes = new ArrayList<ConfigAttribute>(permissions.length);

      for (Permission permission : permissions) {
        attributes.add(new SecurityConfig(permission.toString()));
      }

      return attributes;
    }
  }
}
