package com.xinra.reviewcommunity;

import com.xinra.nucleus.entity.BaseEntity;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class SampleContainerEntity extends BaseEntity {

  @OneToMany(fetch = FetchType.EAGER)
  private Set<SampleMarketSpecificEntity> items;
  
}

