package com.xinra.reviewcommunity.entity;

import javax.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Brand extends SerialEntity {

  private String name;
  private int serial;

}