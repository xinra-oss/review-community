package com.xinra.reviewcommunity.entity;

import java.time.ZonedDateTime;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
  private @NonNull ZonedDateTime createdAt;
  
  @ManyToOne(optional = false)
  private @NonNull Product product;
  
  @ManyToOne(optional = false)
  private @NonNull User user;

  @OneToMany(mappedBy = "review", cascade = CascadeType.REMOVE)
  private @NonNull Set<ReviewComment> comments;

  @OneToMany(mappedBy = "review", cascade = CascadeType.REMOVE)
  private @NonNull Set<ReviewVote> votes;
  
  @PrePersist
  private void persist() {
    if (createdAt == null) {
      createdAt = ZonedDateTime.now();
    }
  }
  
  // cache
  private int numUpvotes;
  private int numDownvotes;
  private double score;
  
}
