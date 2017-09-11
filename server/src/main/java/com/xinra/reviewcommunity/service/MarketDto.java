package com.xinra.reviewcommunity.service;

import com.xinra.nucleus.entity.EntityPk;
import com.xinra.nucleus.service.Dto;
import lombok.Data;

@Data
public class MarketDto implements Dto {

  private EntityPk pk;
  private String slug;
  
}
