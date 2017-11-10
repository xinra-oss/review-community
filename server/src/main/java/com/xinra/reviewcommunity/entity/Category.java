package com.xinra.reviewcommunity.entity;

import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.search.annotations.Field;

@Entity
@Getter
@Setter
public class Category extends SerialEntity {

  @Field
  private String name;

  @ManyToOne
  private Category parent;

  @OneToMany(mappedBy = "parent")
  private Set<Category> children;

}
