package com.xinra.reviewcommunity.shared.dto;


import com.xinra.reviewcommunity.shared.UserLevel;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(of = "name")
public class UserDto implements Dto {

  private @NonNull String name;
  private @NonNull
  UserLevel level;
  
}
