package com.xinra.reviewcommunity.entity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Store;

@Entity
@Getter
@Setter
@Indexed
public class Product extends SerialEntity {

  public static final String REVIEW = "review";

  @Field(store = Store.YES)
  private String name;
  
  @Field(store = Store.YES)
  private String description;

  @IndexedEmbedded
  @ManyToOne(optional = false)
  private @NonNull Category category;
  
  @ManyToOne
  @IndexedEmbedded
  private Brand brand;
  
  // cache
  
  @Field(index = Index.NO, store = Store.YES)
  private double avgRating;
  
  @Field(index = Index.NO, store = Store.YES)
  private int numRatings;
  
  @Field(index = Index.NO, store = Store.YES)
  private double score;

}
