package com.xinra.reviewcommunity.entity;

import javax.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Store;

@Entity
@Getter
@Setter
public class Brand extends MarketSpecificEntity {
  @Field(store = Store.YES)
  private String name;
}
