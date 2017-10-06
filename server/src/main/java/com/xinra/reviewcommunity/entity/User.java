package com.xinra.reviewcommunity.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "\"User\"")
public class User extends NamedEntity {

  private String email;
  
}
