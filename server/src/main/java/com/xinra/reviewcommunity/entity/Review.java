package com.xinra.reviewcommunity.entity;

import java.time.ZonedDateTime;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Review extends SerialEntity {

  private String title;
  private int rating;
  private String text;
  private @NonNull ZonedDateTime dateCreated;
  
  @ManyToOne(optional = false)
  private @NonNull Product product;
  
  @ManyToOne(optional = false)
  private @NonNull User user;
  
  @PrePersist
  private void persist() {
    if (dateCreated == null) {
      dateCreated = ZonedDateTime.now();
    }
  }
  
  // cache
  private int numUpvotes;
  private int numDownvotes;
  private double score;
  
}
