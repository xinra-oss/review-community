package com.xinra.reviewcommunity.shared.dto;

import com.xinra.reviewcommunity.shared.Permission;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import lombok.Data;

@Data
public class InitDto implements Dto {

  private CsrfTokenDto csrfToken;
  private UserDto authenticatedUser;
  private Set<Permission> permissions;
  private Collection<MarketDto> markets;
  private List<CategoryDto> categoryTree;
  // TODO languages
  
}
