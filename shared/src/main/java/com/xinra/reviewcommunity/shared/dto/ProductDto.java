package com.xinra.reviewcommunity.shared.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDto extends SerialDto {

  private String name;
  private String description;
  private int categorySerial;
  private BrandDto brand;
  private int numRatings;
  private double avgRating;
  
}
