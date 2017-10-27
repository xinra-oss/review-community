package com.xinra.reviewcommunity.dto;

import com.xinra.nucleus.service.Dto;
import com.xinra.reviewcommunity.auth.Permission;
import java.util.List;
import java.util.Set;
import lombok.Data;
import org.springframework.security.web.csrf.CsrfToken;

@Data
public class InitDto implements Dto {

  private CsrfToken csrfToken;
  private UserDto authenticatedUser;
  private Set<Permission> permissions;
  private List<MarketDto> markets;
  private List<CategoryDto> categoryTree;
  // TODO languages
  
}
