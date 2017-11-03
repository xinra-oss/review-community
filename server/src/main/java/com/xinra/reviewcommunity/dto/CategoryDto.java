package com.xinra.reviewcommunity.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryDto extends SerialDto {

  private String name;
  private List<CategoryDto> children;
  
}
