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
public class Vote extends BaseEntity {

  private boolean isUpvote;

  @ManyToOne(optional = false)
  private @NonNull User user;

  @ManyToOne(optional = false)
  private @NonNull Review review;

}
