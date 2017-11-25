package com.xinra.reviewcommunity.shared.dto;

import java.util.Collection;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryDto extends SerialDto {

  private int parentSerial;
  private String name;
  private int numProducts;
  private Collection<CategoryDto> children;
  
}
