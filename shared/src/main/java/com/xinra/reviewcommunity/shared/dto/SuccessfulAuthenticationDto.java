package com.xinra.reviewcommunity.shared.dto;

import com.xinra.reviewcommunity.shared.Permission;

import java.util.Set;
import lombok.Data;

@Data
public class SuccessfulAuthenticationDto implements Dto {

  private UserDto user;
  private Set<Permission> permissions;
  private CsrfTokenDto csrfToken;
  
}
