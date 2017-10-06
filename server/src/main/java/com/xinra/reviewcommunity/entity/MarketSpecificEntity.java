package com.xinra.reviewcommunity.entity;

import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

@Getter
@Setter
@MappedSuperclass
@FilterDef(name = "market", parameters = @ParamDef(name = "marketId", type = "string"), 
    defaultCondition = "market_id = :marketId")
@Filter(name = "market")
public abstract class MarketSpecificEntity extends NamedEntity {

  @ManyToOne(fetch = FetchType.LAZY)
  protected Market market;
  
}
