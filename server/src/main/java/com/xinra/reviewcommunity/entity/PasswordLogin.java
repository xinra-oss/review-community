package com.xinra.reviewcommunity.entity;

import javax.persistence.Entity;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Entity
@Getter
@Setter
public class PasswordLogin extends Login {

  private @NonNull String passwordHash;
  
}
