package com.xinra.reviewcommunity.service;

import com.xinra.nucleus.entity.EntityPk;
import com.xinra.reviewcommunity.auth.Permission;
import com.xinra.reviewcommunity.auth.Role;
import java.util.Set;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class AuthenticatedUserDto extends UserDto {

  private @NonNull EntityPk pk;
  private @NonNull Set<Permission> permissions;
  private @NonNull Set<Role> roles;
  
}
