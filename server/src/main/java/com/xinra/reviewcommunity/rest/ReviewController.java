package com.xinra.reviewcommunity.rest;

import com.xinra.reviewcommunity.auth.AccessRequires;
import com.xinra.reviewcommunity.service.ReviewService;
import com.xinra.reviewcommunity.shared.Permission;
import com.xinra.reviewcommunity.shared.dto.CreateReviewCommentDto;
import com.xinra.reviewcommunity.shared.dto.CreateReviewDto;
import com.xinra.reviewcommunity.shared.dto.ReviewCommentDto;
import com.xinra.reviewcommunity.shared.dto.ReviewDto;
import com.xinra.reviewcommunity.shared.dto.ReviewVoteDto;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import javax.validation.Valid;

@RestController
@RequestMapping("/product/{serial}/review")
public class ReviewController extends AbstractController {


  /**
   * Create a new review.
   */
  @AccessRequires(Permission.CREATE_REVIEW)
  @RequestMapping(path = "", method = RequestMethod.POST)
  public void create(@RequestBody @Valid CreateReviewDto createReviewDto,
                     @PathVariable int serial) {
    serviceProvider.getService(ReviewService.class).createReview(createReviewDto, serial);
  }

  /**
   * GET a list of all reviews for a specific product.
   */
  @RequestMapping(path = "", method = RequestMethod.GET)
  public List<ReviewDto> getAllReviews(@PathVariable int serial, @RequestParam String orderBy) {
    return serviceProvider.getService(ReviewService.class).getAllReviews(orderBy, serial);
  }

  /**
   * Create or Updates an upvote for a review.
   */
  @RequestMapping(path = "/{reviewSerial}/vote", method = RequestMethod.POST)
  public void vote(@RequestBody ReviewVoteDto reviewVoteDto,
                   @PathVariable int reviewSerial,
                   @PathVariable int serial ) {

    serviceProvider.getService(ReviewService.class).vote(reviewVoteDto, reviewSerial, serial);
  }

  /**
   * Creates a Comment for a Review.
   */
  @AccessRequires(Permission.CREATE_REVIEW_COMMENT)
  @RequestMapping(path = "/{reviewSerial}/comment", method = RequestMethod.POST)
  public void comment(@RequestBody CreateReviewCommentDto createReviewCommentDto,
                      @PathVariable int reviewSerial,
                      @PathVariable int serial) {
    serviceProvider.getService(ReviewService.class).createReviewComment(createReviewCommentDto,
            reviewSerial, serial);
  }

  /**
   * GET a list of all Comments for a specific Review.
   */
  @RequestMapping(path = "/{reviewSerial}/comment", method = RequestMethod.GET)
  public List<ReviewCommentDto> getAllReviewComments(@PathVariable int reviewSerial, @PathVariable int serial) {
    return serviceProvider.getService(ReviewService.class).getAllReviewComments(reviewSerial, serial);
  }

  /**
   * Delete a Comment.
   */
  //TODO Permission DELETE_COMMENT
  @RequestMapping(path = "/{reviewSerial}/comment/{commentSerial}", method = RequestMethod.DELETE)
  public void deleteComment(@PathVariable int reviewSerial, @PathVariable int commentSerial) {

  }

  /**
   * Delete a Review.
   */
  //TODO Permission DELETE_REVIEW
  @RequestMapping(path = "/{reviewSerial}", method = RequestMethod.DELETE)
  public void deleteReview(@PathVariable int reviewSerial) {

  }

}
