package com.xinra.reviewcommunity.dto;

import com.xinra.nucleus.service.Dto;
import com.xinra.reviewcommunity.entity.Brand;
import com.xinra.reviewcommunity.entity.Category;
import lombok.Data;

@Data
public class ProductDto implements Dto {

  private String name;
  private String description;
  private CategoryDto categoryDto;
  private BrandDto brandDto;
}
