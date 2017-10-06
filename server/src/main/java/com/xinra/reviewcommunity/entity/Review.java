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
public class Review extends BaseEntity {
  
  private int rating;
  private String text;
  
  @ManyToOne(optional = false)
  private @NonNull Product product;
  
}
