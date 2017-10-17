package com.xinra.reviewcommunity.dto;

import com.xinra.nucleus.service.Dto;
import lombok.Data;

@Data
public class ProductDto implements Dto {

  private String name;
  private String description;
  private CategoryDto category;
  private BrandDto brand;
  
}
