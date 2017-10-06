package com.xinra.reviewcommunity.entity;

import com.xinra.nucleus.entity.BaseEntity;
import javax.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class NamedEntity extends BaseEntity {
  
  private @NonNull String name;
  
}
