package com.xinra.reviewcommunity.dto;

import com.xinra.reviewcommunity.entity.Category;
import java.util.List;
import lombok.Data;

@Data
public class CategoryDto extends SerialDto {

  private String name;
  private List<Category> children;
  
}
