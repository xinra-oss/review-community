package com.xinra.reviewcommunity.rest;

import com.xinra.reviewcommunity.auth.AccessRequires;
import com.xinra.reviewcommunity.auth.Permission;
import com.xinra.reviewcommunity.dto.CreateReviewDto;
import com.xinra.reviewcommunity.dto.ReviewDto;
import com.xinra.reviewcommunity.dto.VoteDto;
import com.xinra.reviewcommunity.service.ReviewService;
import java.util.List;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
   * GET a list of all reviews.
   */
  @RequestMapping(path = "", method = RequestMethod.GET)
  public List<ReviewDto> getAll(@RequestHeader String order, @RequestHeader String sortBy) {
    return serviceProvider.getService(ReviewService.class).getAllReviews(sortBy, order);
  }

  /**
   * Create or Updates an upvote for a review.
   */
  @RequestMapping(path = "/{serial}/vote", method = RequestMethod.POST)
  public void vote(@RequestBody VoteDto voteDto, @PathVariable int serial) {
    serviceProvider.getService(ReviewService.class).vote(voteDto, serial);
  }

}
