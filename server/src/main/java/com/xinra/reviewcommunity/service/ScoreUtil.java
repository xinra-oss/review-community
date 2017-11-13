package com.xinra.reviewcommunity.service;

/**
 * The score used to sort products and reviews by rating/votes is the lower bound of Wilson score
 * confidence interval for a Bernoulli parameter (0.9604). For details refer to
 * https://github.com/xinra-it/review-community/wiki/Sorting-by-Score
 */
// This is adapted from https://gist.github.com/julienbourdeau/77eaca0fd1e4af3fde9fe018fdf13d7d
public class ScoreUtil {
  
  /**
   * Calculates the score by number of up- and downvotes.
   */
  public static double fromVotes(double numUpvotes, double numDownvotes) {
    if (numDownvotes == 0 && numDownvotes == 0) {
      return 0;
    }
    return (((numUpvotes + 1.9208) / (numUpvotes + numDownvotes) 
        - 1.96 * Math.sqrt(((numUpvotes * numDownvotes) / (numUpvotes + numDownvotes)) + 0.9604) 
        / (numUpvotes + numDownvotes)) / (1 + 3.8416 / (numUpvotes + numDownvotes)));
  }
  
  /**
   * Calculates score by number of different five star ratings.
   */
  public static double fromRatings(int numOne, int numTwo, int numThree, int numFour, int numFive) {
    double numUpvotes = numTwo * 0.25 + numThree * 0.5 + numFour * 0.75 + numFive;
    double numDownvotes = numOne + numTwo * 0.75 + numThree * 0.5 + numFour * 0.25;
    return fromVotes(numUpvotes, numDownvotes);
  }
  
  /**
   * Calculates score by average (five star) rating and total number of ratings.
   */
  public static double fromAverageRating(double avgRating, int numRatings) {
    double numUpvotes = (avgRating * numRatings - numRatings) / 4;
    double numDownvotes = numRatings - numUpvotes;
    return fromVotes(numUpvotes, numDownvotes);
  }

}
