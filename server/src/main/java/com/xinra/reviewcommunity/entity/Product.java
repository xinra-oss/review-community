package com.xinra.reviewcommunity.entity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Product extends NamedEntity {
  
  private String description;
  
  @ManyToOne(optional = false)
  private @NonNull Category category;
  
  @ManyToOne
  private Brand brand;
  
  // cache
  private double ratingAverage;
  private int ratingAmount;

}
