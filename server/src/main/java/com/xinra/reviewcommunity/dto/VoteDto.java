package com.xinra.reviewcommunity.dto;

import com.xinra.nucleus.service.Dto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VoteDto implements Dto {

  private boolean upvote;
}
