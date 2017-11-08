package com.xinra.reviewcommunity.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.data.Offset;
import org.junit.Test;

public class TestScoreUtil {
  
  private static final Offset<Double> OFFSET = Offset.offset(0.0000000000001);
  
  // control values are from https://gist.github.com/julienbourdeau/77eaca0fd1e4af3fde9fe018fdf13d7d
  
  @Test
  public void fromRatings() {
    assertThat(ScoreUtil.fromRatings(0, 0, 0, 0, 0)).isCloseTo(0, OFFSET);
    assertThat(ScoreUtil.fromRatings(10, 1, 2, 6, 90)).isCloseTo(0.80390178246001, OFFSET);
    assertThat(ScoreUtil.fromRatings(80, 1, 2, 6, 90)).isCloseTo(0.46188074417216, OFFSET);
    assertThat(ScoreUtil.fromRatings( 0, 1, 2, 6, 0 )).isCloseTo(0.33136280289755, OFFSET);
    assertThat(ScoreUtil.fromRatings(10, 1, 2, 0, 2 )).isCloseTo(0.079648861762752, OFFSET);
  }
  
  @Test
  public void fromAverageRating() {
    assertThat(ScoreUtil.fromVotes(0, 0)).isCloseTo(0, OFFSET);
    assertThat(ScoreUtil.fromAverageRating(4.8000001907349, 10))
      .isCloseTo(0.65545605272928, OFFSET);
  }
  
  @Test
  public void fromVotes() {
    assertThat(ScoreUtil.fromVotes(0, 0)).isCloseTo(0, OFFSET);
  }


}
