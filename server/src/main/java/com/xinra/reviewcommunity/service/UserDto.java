package com.xinra.reviewcommunity.service;

import com.xinra.nucleus.service.Dto;
import com.xinra.reviewcommunity.entity.UserLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(of = "name")
public class UserDto implements Dto {

  private @NonNull String name;
  private @NonNull UserLevel level;
  
}
