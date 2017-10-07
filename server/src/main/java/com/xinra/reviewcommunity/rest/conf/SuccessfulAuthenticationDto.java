package com.xinra.reviewcommunity.rest.conf;

import com.xinra.nucleus.service.Dto;
import com.xinra.reviewcommunity.entity.Permission;
import com.xinra.reviewcommunity.service.UserDto;
import java.util.Set;
import lombok.Data;
import org.springframework.security.web.csrf.CsrfToken;

@Data
public class SuccessfulAuthenticationDto implements Dto {

  private UserDto user;
  private Set<Permission> permissions;
  private CsrfToken csrfToken;
  
}