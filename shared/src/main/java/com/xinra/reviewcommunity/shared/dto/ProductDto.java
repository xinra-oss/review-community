package com.xinra.reviewcommunity.shared.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDto extends SerialDto {

  private String name;
  private String description;
  private CategoryDto category;
  private BrandDto brand;
  
}
