package com.xinra.reviewcommunity.entity;

import com.xinra.nucleus.entity.BaseEntity;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Entity
@Getter
@Setter
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Login extends BaseEntity {

  @ManyToOne(optional = false)
  private @NonNull User user;
  
}
