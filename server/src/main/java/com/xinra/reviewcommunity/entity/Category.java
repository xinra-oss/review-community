package com.xinra.reviewcommunity.entity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Category extends NamedEntity {
  
  @ManyToOne
  private Category parent;

}
