package com.xinra.reviewcommunity.dto;

import com.xinra.nucleus.service.Dto;
import lombok.Data;

@Data
public class CreateReviewCommentDto implements Dto {

  private String text;
}
