package com.xinra.reviewcommunity.service;

import com.google.common.collect.Streams;
import com.xinra.reviewcommunity.dto.CreateReviewCommentDto;
import com.xinra.reviewcommunity.dto.CreateReviewDto;
import com.xinra.reviewcommunity.dto.ReviewCommentDto;
import com.xinra.reviewcommunity.dto.ReviewDto;
import com.xinra.reviewcommunity.dto.UserDto;
import com.xinra.reviewcommunity.dto.VoteDto;
import com.xinra.reviewcommunity.entity.OrderBy;
import com.xinra.reviewcommunity.entity.Product;
import com.xinra.reviewcommunity.entity.Review;
import com.xinra.reviewcommunity.entity.ReviewComment;
import com.xinra.reviewcommunity.entity.User;
import com.xinra.reviewcommunity.entity.Vote;
import com.xinra.reviewcommunity.repo.ProductRepository;
import com.xinra.reviewcommunity.repo.ReviewCommentRepository;
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
  private @Autowired ReviewCommentRepository<ReviewComment> reviewCommentRepo;
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

    Review existingReview = reviewRepo.findByUserIdAndProductId(user.getPk().getId(),
                                                                product.getPk().getId());
    if (existingReview != null) {
      throw new ReviewAlreadyExistsException(Review.class,
              existingReview.getSerial(),
              user.getName());
    }

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
  public List<ReviewDto> getAllReviews(String oderBy, int productSerial) {

    Product product = productRepo.findBySerial(productSerial);
    if (product == null) {
      throw new SerialNotFoundException(Product.class, productSerial);
    }
    List<ReviewDto> list;

    if (oderBy.equals(OrderBy.DATE.name())) {
      list = Streams.stream(reviewRepo.findByProductIdOrderByCreatedAtDesc(product.getPk().getId()))
              .map(this::reviewToDto)
              .collect(Collectors.toList());
    } else if (oderBy.equals(OrderBy.RATING.name())) {
      list = Streams.stream(reviewRepo.findByProductIdOrderByRatingDesc(product.getPk().getId()))
              .map(this::reviewToDto)
              .collect(Collectors.toList());
    } else {
      list = Streams.stream(reviewRepo.findByProductId(product.getPk().getId()))
            .map(this::reviewToDto)
            .collect(Collectors.toList());
    }
    return list;
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

  /**
   * Creates a new comment for a specific review.
   */
  public void createReviewComment(CreateReviewCommentDto createReviewCommentDto, int reviewSerial) {

    User user = userRepo.findOne(contextHolder.get().getAuthenticatedUser().get().getPk());
    Review review = reviewRepo.findBySerial(reviewSerial);
    ReviewComment reviewComment = entityFactory.createEntity(ReviewComment.class);

    reviewComment.setReview(review);
    reviewComment.setUser(user);
    reviewComment.setText(createReviewCommentDto.getText());

    int serial = serviceProvider.getService(SerialService.class).getNextSerial(ReviewComment.class);
    reviewComment.setSerial(serial);
    reviewCommentRepo.save(reviewComment);

    log.info("Created ReviewComment for Review with serial '{}'", reviewSerial);

  }

  /**
   * Returns all Comments for a specific Review.
   */
  public List<ReviewCommentDto> getAllReviewComments(int reviewSerial) {

    Review review = reviewRepo.findBySerial(reviewSerial);
    if (review == null) {
      throw new SerialNotFoundException(Review.class, reviewSerial);
    }
    List<ReviewCommentDto> list = Streams.stream(reviewCommentRepo
            .findByReviewIdOrderByCreatedAtAsc(review.getPk()
            .getId()))
            .map(this::reviewCommentToDto)
            .collect(Collectors.toList());

    return list;
  }

  private ReviewDto reviewToDto(Review review) {
    ReviewDto reviewDto = dtoFactory.createDto(ReviewDto.class);

    UserDto userDto = serviceProvider.getService(UserService.class).toDto(review.getUser());

    reviewDto.setUserDto(userDto);
    reviewDto.setSerial(review.getSerial());
    reviewDto.setCreatedAt(review.getCreatedAt());
    reviewDto.setRating(review.getRating());
    reviewDto.setText(review.getText());
    reviewDto.setTitle(review.getTitle());
    reviewDto.setNumDownvotes(review.getNumDownvotes());
    reviewDto.setNumUpvotes(review.getNumUpvotes());
    reviewDto.setScore(review.getScore());

    return reviewDto;
  }

  private ReviewCommentDto reviewCommentToDto(ReviewComment reviewComment) {

    ReviewCommentDto reviewCommentDto = dtoFactory.createDto(ReviewCommentDto.class);
    UserDto userDto = serviceProvider.getService(UserService.class).toDto(reviewComment.getUser());

    reviewCommentDto.setUserDto(userDto);
    reviewCommentDto.setText(reviewComment.getText());
    reviewCommentDto.setCreatedAt(reviewComment.getCreatedAt());
    reviewCommentDto.setSerial(reviewComment.getSerial());

    return reviewCommentDto;
  }

}
