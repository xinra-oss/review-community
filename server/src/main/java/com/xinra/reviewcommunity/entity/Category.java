package com.xinra.reviewcommunity.entity;

import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Category extends SerialEntity {

  private String name;

  @ManyToOne
  private Category parent;

  @OneToMany(mappedBy = "parent")
  private Set<Category> children;

}
