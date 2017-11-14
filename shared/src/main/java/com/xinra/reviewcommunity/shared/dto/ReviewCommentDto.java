package com.xinra.reviewcommunity.shared.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewCommentDto extends SerialDto {

  private String text;

  //@JsonFormat(pattern = JsonUtil.ZONED_DATE_FORMAT)
  private Date createdAt;

  private UserDto userDto;


}
