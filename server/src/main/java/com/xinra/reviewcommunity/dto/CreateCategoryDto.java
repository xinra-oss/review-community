package com.xinra.reviewcommunity.dto;

import com.xinra.nucleus.service.Dto;
import lombok.Data;

@Data
public class CreateCategoryDto implements Dto {

  private String name;
  private int parentSerial;
}
