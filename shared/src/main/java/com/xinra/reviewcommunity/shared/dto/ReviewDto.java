package com.xinra.reviewcommunity.shared.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xinra.reviewcommunity.shared.dto.SerialDto;
import com.xinra.reviewcommunity.shared.dto.UserDto;

import java.time.ZonedDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewDto extends SerialDto {

  private String title;
  private int rating;
  private String text;

  @JsonFormat(pattern = JsonUtil.ZONED_DATE_FORMAT)
  private ZonedDateTime createdAt;

  private int productSerial;

  private UserDto userDto;
  private int numUpvotes;
  private int numDownvotes;
  private double score;

}
