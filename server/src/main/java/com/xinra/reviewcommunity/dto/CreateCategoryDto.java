package com.xinra.reviewcommunity.dto;

import com.xinra.nucleus.service.Dto;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

@Data
public class CreateCategoryDto implements Dto {

  @NotBlank
  @Length(max = 30)
  private String name;

  private int parentSerial;
}
