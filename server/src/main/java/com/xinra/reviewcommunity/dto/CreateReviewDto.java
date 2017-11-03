package com.xinra.reviewcommunity.dto;

import com.xinra.nucleus.service.Dto;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

@Data
public class CreateReviewDto implements Dto {

  @Length(max = 50)
  private String title;

  @Range(min = 0, max = 5)
  private int rating;

  private String text;

}
