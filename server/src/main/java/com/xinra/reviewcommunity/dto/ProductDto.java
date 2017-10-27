package com.xinra.reviewcommunity.dto;

import lombok.Data;

@Data
public class ProductDto extends SerialDto {

  private String name;
  private String description;
  private CategoryDto category;
  private BrandDto brand;
  
}
