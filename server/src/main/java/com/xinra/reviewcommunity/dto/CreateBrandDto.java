package com.xinra.reviewcommunity.dto;

import com.xinra.nucleus.service.Dto;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

@Data
public class CreateBrandDto implements Dto {

  @NotBlank
  @Length(max = 50)
  private String name;

}
