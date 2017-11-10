package com.xinra.reviewcommunity.entity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Product extends SerialEntity {

  public static final String REVIEW = "review";

  private String name;
  private String description;

  private String barcode;

  @ManyToOne(optional = false)
  private @NonNull Category category;
  
  @ManyToOne
  private Brand brand;
  
  // cache
  private double avgRating;
  private int numRatings;
  private double score;

}
