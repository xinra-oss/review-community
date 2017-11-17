package com.xinra.reviewcommunity.service;

import com.xinra.nucleus.common.ContextHolder;
import com.xinra.reviewcommunity.Context;
import com.xinra.reviewcommunity.entity.Product;
import com.xinra.reviewcommunity.entity.Review;
import com.xinra.reviewcommunity.entity.ReviewComment;
import com.xinra.reviewcommunity.entity.ReviewVote;
import com.xinra.reviewcommunity.entity.User;
import com.xinra.reviewcommunity.repo.ProductRepository;
import com.xinra.reviewcommunity.repo.ReviewCommentRepository;
import com.xinra.reviewcommunity.repo.ReviewRepository;
import com.xinra.reviewcommunity.repo.ReviewVoteRepository;
import com.xinra.reviewcommunity.repo.UserRepository;
import com.xinra.reviewcommunity.shared.OrderBy;
import com.xinra.reviewcommunity.shared.dto.CreateReviewCommentDto;
import com.xinra.reviewcommunity.shared.dto.CreateReviewDto;
import com.xinra.reviewcommunity.shared.dto.ReviewCommentDto;
import com.xinra.reviewcommunity.shared.dto.ReviewDto;
import com.xinra.reviewcommunity.shared.dto.ReviewVoteDto;
import com.xinra.reviewcommunity.shared.dto.UserDto;
import java.util.Date;
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
  private @Autowired ReviewVoteRepository<ReviewVote> voteRepo;
  private @Autowired ContextHolder<Context> contextHolder;

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
    product.setScore(ScoreUtil.fromAverageRating(avgRatingNew, numRatings));

    int serial = serviceProvider.getService(SerialService.class)
            .getNextChildSerial(product.getPk(), Product.REVIEW);
    review.setSerial(serial);
    reviewRepo.save(review);

    log.info("Created Review for product with serial '{}'", productSerial);
  }

  /**
   * Returns a list of all reviews.
   */
  @SuppressWarnings("unchecked")
  public List<ReviewDto> getReviewsForProduct(int productSerial, @NonNull OrderBy orderBy) {

    Product product = productRepo.findBySerial(productSerial);
    if (product == null) {
      throw new SerialNotFoundException(Product.class, productSerial);
    }
    
    List<?> results = reviewRepo.findByProduct(product, orderBy);
    
    if (contextHolder.get().getAuthenticatedUser().isPresent()) {
      return ((List<Object[]>) results).stream()
          .map(r -> reviewToDto((Review) r[0], (ReviewVote) r[1]))
          .collect(Collectors.toList());
    } else {
      return results.stream()
          .map(r -> reviewToDto((Review) r))
          .collect(Collectors.toList());
    }
  }

  /**
   * Deletes a review.
   */
  public void deleteReview(int reviewSerial, int productSerial) {
    Review review = reviewRepo.findBySerialAndProductSerial(reviewSerial, productSerial);
    reviewRepo.delete(review);
  }

  /**
   * Creates or uptates the upvotes for a review.
   */
  public void vote(ReviewVoteDto reviewVoteDto, int reviewSerial, int productSerial) {

    User user = userRepo.findOne(contextHolder.get().getAuthenticatedUser().get().getPk());
    Review review = reviewRepo.findBySerialAndProductSerial(reviewSerial, productSerial);
    ReviewVote vote 
        = voteRepo.findByUserIdAndReviewId(user.getPk().getId(), review.getPk().getId());

    int numUpvotes = review.getNumUpvotes();
    int numDownvotes = review.getNumDownvotes();

    if (vote != null) {
      if (vote.isUpvote() && !reviewVoteDto.isUpvote()) {
        review.setNumUpvotes(numUpvotes - 1);
        review.setNumDownvotes(numDownvotes + 1);
      } else if (!vote.isUpvote() && reviewVoteDto.isUpvote()) {
        review.setNumUpvotes(numUpvotes + 1);
        review.setNumDownvotes(numDownvotes - 1);
      }
      vote.setUpvote(reviewVoteDto.isUpvote());
    } else {
      vote = entityFactory.createEntity(ReviewVote.class);
      vote.setUpvote(reviewVoteDto.isUpvote());
      vote.setReview(review);
      vote.setUser(user);
      if (reviewVoteDto.isUpvote()) {
        review.setNumUpvotes(numUpvotes + 1);
      } else {
        review.setNumDownvotes(numDownvotes + 1);
      }
    }

    review.setScore(ScoreUtil.fromVotes((double) numUpvotes, (double) numDownvotes));

    voteRepo.save(vote);
  }

  /**
   * Creates a new comment for a specific review.
   */
  public void createReviewComment(CreateReviewCommentDto createReviewCommentDto, int reviewSerial,
      int productSerial) {

    User user = userRepo.findOne(contextHolder.get().getAuthenticatedUser().get().getPk());
    Review review = reviewRepo.findBySerialAndProductSerial(reviewSerial, productSerial);
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
  public List<ReviewCommentDto> getAllReviewComments(int reviewSerial, int productSerial) {

    Review review = reviewRepo.findBySerialAndProductSerial(reviewSerial, productSerial);

    if (review == null) {
      throw new SerialNotFoundException(Review.class, reviewSerial);
    }
    
    return reviewCommentRepo.findByReviewIdOrderByCreatedAtAsc(review.getPk().getId())
      .stream()
      .map(this::reviewCommentToDto)
      .collect(Collectors.toList());
  }

  /**
   * Deletes a reviewComment.
   */
  public void deleteReviewComment(int reviewCommentSerial, int reviewSerial) {
    ReviewComment reviewComment = reviewCommentRepo
            .findBySerialAndReviewSerial(reviewCommentSerial, reviewSerial);
    // todo error handling
    reviewCommentRepo.delete(reviewComment);
  }
  
  /**
   * Converts a review entity to a DTO.
   */
  private ReviewDto reviewToDto(Review review) {
    ReviewDto reviewDto = dtoFactory.createDto(ReviewDto.class);

    UserDto userDto = serviceProvider.getService(UserService.class).toDto(review.getUser());

    reviewDto.setUserDto(userDto);
    reviewDto.setSerial(review.getSerial());
    reviewDto.setCreatedAt(Date.from(review.getCreatedAt().toInstant()));
    reviewDto.setRating(review.getRating());
    reviewDto.setText(review.getText());
    reviewDto.setTitle(review.getTitle());
    reviewDto.setNumDownvotes(review.getNumDownvotes());
    reviewDto.setNumUpvotes(review.getNumUpvotes());
    reviewDto.setScore(review.getScore());

    return reviewDto;
  }
  
  private ReviewDto reviewToDto(Review review, ReviewVote vote) {
    ReviewDto reviewDto = reviewToDto(review);
    if (vote != null) {
      reviewDto.setAuthenticatedUserVote(reviewVoteToDto(vote));
    }
    return reviewDto;
  }
  
  private ReviewVoteDto reviewVoteToDto(ReviewVote vote) {
    ReviewVoteDto voteDto = dtoFactory.createDto(ReviewVoteDto.class);
    voteDto.setUpvote(vote.isUpvote());
    return voteDto;
  }

  private ReviewCommentDto reviewCommentToDto(ReviewComment reviewComment) {

    ReviewCommentDto reviewCommentDto = dtoFactory.createDto(ReviewCommentDto.class);
    UserDto userDto = serviceProvider.getService(UserService.class).toDto(reviewComment.getUser());

    reviewCommentDto.setUserDto(userDto);
    reviewCommentDto.setText(reviewComment.getText());
    reviewCommentDto.setCreatedAt(Date.from(reviewComment.getCreatedAt().toInstant()));
    reviewCommentDto.setSerial(reviewComment.getSerial());

    return reviewCommentDto;
  }

}
