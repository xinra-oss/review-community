package com.xinra.reviewcommunity.dto;

import com.xinra.nucleus.service.Dto;
import lombok.Data;

@Data
public class CategoryDto implements Dto {

  private String categoryId;
  private String name;
  
}
