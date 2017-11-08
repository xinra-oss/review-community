package com.xinra.reviewcommunity.dto;

import com.xinra.nucleus.entity.EntityPk;
import com.xinra.reviewcommunity.shared.Permission;
import com.xinra.reviewcommunity.shared.Role;
import com.xinra.reviewcommunity.shared.dto.UserDto;
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
