package com.xinra.reviewcommunity.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDto extends SerialDto {

  private String name;
  private String description;
  private CategoryDto category;
  private BrandDto brand;
  private int numRatings;
  private double avgRating;
  
}
