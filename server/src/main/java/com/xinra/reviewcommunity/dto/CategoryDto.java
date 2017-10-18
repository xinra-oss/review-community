package com.xinra.reviewcommunity.dto;

import java.util.List;
import lombok.Data;

@Data
public class CategoryDto extends SerialDto {

  private String name;
  private List<CategoryDto> children;
  
}
