package com.xinra.reviewcommunity.entity;

import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "\"User\"")
public class User extends NamedEntity {

  private String email;
  
  @Enumerated(EnumType.STRING)
  @ElementCollection(targetClass = Role.class)
  private Set<Role> roles;
  
  @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
  private Set<Login> logins;
  
}
