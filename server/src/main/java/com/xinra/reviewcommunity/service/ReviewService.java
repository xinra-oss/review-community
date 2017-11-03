package com.xinra.reviewcommunity.service;

import com.google.common.collect.Streams;
import com.xinra.reviewcommunity.dto.CreateReviewDto;
import com.xinra.reviewcommunity.dto.ReviewDto;
import com.xinra.reviewcommunity.dto.UserDto;
import com.xinra.reviewcommunity.dto.VoteDto;
import com.xinra.reviewcommunity.entity.Product;
import com.xinra.reviewcommunity.entity.Review;
import com.xinra.reviewcommunity.entity.User;
import com.xinra.reviewcommunity.entity.Vote;
import com.xinra.reviewcommunity.repo.ProductRepository;
import com.xinra.reviewcommunity.repo.ReviewRepository;
import com.xinra.reviewcommunity.repo.UserRepository;
import com.xinra.reviewcommunity.repo.VoteRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
public class ReviewService extends AbstractService {

  private @Autowired ReviewRepository<Review> reviewRepo;
  private @Autowired ProductRepository<Product> productRepo;
  private @Autowired UserRepository<User> userRepo;
  private @Autowired VoteRepository<Vote> voteRepo;

  /**
   * Creates a new review.
   */
  public void createReview(@NonNull CreateReviewDto createReviewDto, int productSerial) {

    Product product = productRepo.findBySerial(productSerial);
    if (product == null) {
      throw new SerialNotFoundException(Product.class, productSerial);
    }

    User user = userRepo.findOne(contextHolder.get().getAuthenticatedUser().get().getPk());

    Review review = entityFactory.createEntity(Review.class);
    review.setTitle(createReviewDto.getTitle());
    review.setRating(createReviewDto.getRating());
    review.setText(createReviewDto.getText());
    review.setProduct(product);
    review.setUser(user);

    int numRatings = product.getNumRatings();
    double avgRatingOld = product.getAvgRating();

    double avgRatingNew = (numRatings * avgRatingOld + createReviewDto.getRating())
            / (numRatings + 1);

    product.setNumRatings(numRatings + 1);
    product.setAvgRating(avgRatingNew);

    int serial = serviceProvider.getService(SerialService.class).getNextSerial(Review.class);
    review.setSerial(serial);
    reviewRepo.save(review);

    log.info("Created Review for product with serial '{}'", productSerial);
  }

  /**
   * Returns a list of all reviews.
   */
  public List<ReviewDto> getAllReviews() {
    return Streams.stream(reviewRepo.findAll())
      .map(this::toDto)
      .collect(Collectors.toList());
  }

  private ReviewDto toDto(Review review) {
    ReviewDto reviewDto = dtoFactory.createDto(ReviewDto.class);

    UserDto userDto = serviceProvider.getService(UserService.class).toDto(review.getUser());

    reviewDto.setUserDto(userDto);
    reviewDto.setSerial(review.getSerial());
    reviewDto.setDateCreated(review.getDateCreated());
    reviewDto.setRating(review.getRating());
    reviewDto.setText(review.getText());
    reviewDto.setTitle(review.getTitle());
    reviewDto.setNumDownvotes(review.getNumDownvotes());
    reviewDto.setNumUpvotes(review.getNumUpvotes());
    reviewDto.setScore(review.getScore());

    return reviewDto;
  }

  /**
   * Creates or uptates the upvotes for a review.
   */
  public void vote(VoteDto voteDto, int reviewSerial) {

    User user = userRepo.findOne(contextHolder.get().getAuthenticatedUser().get().getPk());
    Review review = reviewRepo.findBySerial(reviewSerial);
    Vote vote = voteRepo.findByUserIdAndReviewId(user.getPk().getId(), review.getPk().getId());

    if (vote != null) {
      vote.setUpvote(voteDto.isUpvote());
    } else {
      vote = entityFactory.createEntity(Vote.class);
      vote.setUpvote(voteDto.isUpvote());
      vote.setReview(review);
      vote.setUser(user);
    }

    int numUpvotes = review.getNumUpvotes();
    int numDownvotes = review.getNumDownvotes();
    if (voteDto.isUpvote()) {
      review.setNumUpvotes(numUpvotes + 1);
    } else {
      review.setNumDownvotes(numDownvotes + 1);
    }

    voteRepo.save(vote);
  }
}
