package com.xinra.reviewcommunity.shared.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.ZonedDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewCommentDto extends SerialDto {

  private String text;

  @JsonFormat(pattern = JsonUtil.ZONED_DATE_FORMAT)
  private ZonedDateTime createdAt;

  private UserDto userDto;


}
