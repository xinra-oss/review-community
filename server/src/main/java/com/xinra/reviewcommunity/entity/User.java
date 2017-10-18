package com.xinra.reviewcommunity.entity;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.xinra.nucleus.entity.BaseEntity;
import com.xinra.reviewcommunity.auth.Role;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "\"User\"")
public class User extends BaseEntity {

  private String name;
  private String email;
  
  @Enumerated(EnumType.STRING)
  @ElementCollection(targetClass = Role.class)
  private @NonNull Set<Role> roles;
  
  @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
  private @NonNull Set<Login> logins;
  
  // cache
  
  @Enumerated(EnumType.STRING)
  private @NonNull UserLevel level;
  
}
