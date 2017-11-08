package com.xinra.reviewcommunity.shared.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

@Data
public class CreateProductDto implements Dto {

  @NotBlank
  @Length(max = 50)
  private String name;

  private String description;

  @Range(min = 1)
  private int categorySerial;

  private int brandSerial;
  
}
