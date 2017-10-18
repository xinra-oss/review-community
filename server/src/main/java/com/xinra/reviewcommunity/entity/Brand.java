package com.xinra.reviewcommunity.entity;

import javax.persistence.Entity;

import com.xinra.nucleus.entity.BaseEntity;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Brand extends BaseEntity {

  private String name;
  private int serial;

}
