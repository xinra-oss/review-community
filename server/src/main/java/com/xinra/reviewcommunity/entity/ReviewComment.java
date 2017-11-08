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
public class ReviewComment extends SerialEntity {

  private @NonNull String text;
  private @NonNull ZonedDateTime createdAt;
  
  @ManyToOne(optional = false)
  private @NonNull Review review;
  
  @ManyToOne(optional = false)
  private @NonNull User user;
  
  @PrePersist
  private void persist() {
    if (createdAt == null) {
      createdAt = ZonedDateTime.now();
    }
  }
  
}
