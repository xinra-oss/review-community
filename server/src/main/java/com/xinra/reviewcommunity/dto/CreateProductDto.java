package com.xinra.reviewcommunity.dto;

import com.xinra.nucleus.service.Dto;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

@Data
public class CreateProductDto implements Dto {

  @NotBlank
  @Length(max = 50)
  private String name;

  private String description;

  @NotBlank
  private int categorySerial;

  @NotBlank
  private int brandSerial;
  
}
