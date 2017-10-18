package com.xinra.reviewcommunity.entity;

import javax.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ChildSerial extends Serial {

  private String entityId;

}
