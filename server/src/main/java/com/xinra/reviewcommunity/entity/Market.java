package com.xinra.reviewcommunity.entity;

import javax.persistence.Entity;

import com.xinra.nucleus.entity.BaseEntity;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Market extends BaseEntity {

  private String name;
  private @NonNull String slug;
  
}
