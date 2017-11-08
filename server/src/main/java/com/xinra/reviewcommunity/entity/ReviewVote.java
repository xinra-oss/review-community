package com.xinra.reviewcommunity.entity;

import com.xinra.nucleus.entity.BaseEntity;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ReviewVote extends BaseEntity {

  private boolean upvote;
  
  @ManyToOne(optional = false)
  private @NonNull Review review;
  
  @ManyToOne(optional = false)
  private @NonNull User user;
  
}
