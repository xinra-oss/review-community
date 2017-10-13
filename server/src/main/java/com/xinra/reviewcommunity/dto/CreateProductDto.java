package com.xinra.reviewcommunity.dto;

import com.xinra.nucleus.service.Dto;
import lombok.Data;

@Data
public class CreateProductDto implements Dto {

  private String name;
  private String description;
  private String categoryId;
  private String brandId;
}
