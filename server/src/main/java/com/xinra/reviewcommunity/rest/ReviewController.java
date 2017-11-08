package com.xinra.reviewcommunity.rest;

import com.xinra.reviewcommunity.auth.AccessRequires;
import com.xinra.reviewcommunity.auth.Permission;
import com.xinra.reviewcommunity.dto.CreateReviewCommentDto;
import com.xinra.reviewcommunity.dto.CreateReviewDto;
import com.xinra.reviewcommunity.dto.ReviewCommentDto;
import com.xinra.reviewcommunity.dto.ReviewDto;
import com.xinra.reviewcommunity.dto.VoteDto;
import com.xinra.reviewcommunity.service.ReviewService;
import java.util.List;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
  public void vote(@RequestBody VoteDto voteDto, @PathVariable int reviewSerial) {
    serviceProvider.getService(ReviewService.class).vote(voteDto, reviewSerial);
  }

  /**
   * Creates a Comment for a Review.
   */
  @AccessRequires(Permission.CREATE_REVIEW_COMMENT)
  @RequestMapping(path = "/{reviewSerial}/comment", method = RequestMethod.POST)
  public void comment(@RequestBody CreateReviewCommentDto createReviewCommentDto,
                      @PathVariable int reviewSerial) {
    serviceProvider.getService(ReviewService.class).createReviewComment(createReviewCommentDto,
            reviewSerial);
  }

  /**
   * GET a list of all Comments for a specific Review.
   */
  @RequestMapping(path = "/{reviewSerial}/comment", method = RequestMethod.GET)
  public List<ReviewCommentDto> getAllReviewComments(@PathVariable int reviewSerial) {
    return serviceProvider.getService(ReviewService.class).getAllReviewComments(reviewSerial);
  }
}
