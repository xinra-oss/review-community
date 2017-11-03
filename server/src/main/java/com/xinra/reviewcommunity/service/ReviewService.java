package com.xinra.reviewcommunity.service;

import com.google.common.collect.Streams;
import com.xinra.reviewcommunity.dto.CreateReviewDto;
import com.xinra.reviewcommunity.dto.ReviewDto;
import com.xinra.reviewcommunity.dto.UserDto;
import com.xinra.reviewcommunity.entity.Product;
import com.xinra.reviewcommunity.entity.Review;
import com.xinra.reviewcommunity.entity.User;
import com.xinra.reviewcommunity.repo.ProductRepository;
import com.xinra.reviewcommunity.repo.ReviewRepository;
import com.xinra.reviewcommunity.repo.UserRepository;
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

  /**
   * Creates a new review.
   */
  public void createReview(@NonNull CreateReviewDto createReviewDto, int productSerial) {

    Product product = productRepo.findBySerial(productSerial);
    User user = userRepo.findOne(contextHolder.get().getAuthenticatedUser().get().getPk());

    if (product == null) {
      throw new SerialNotFoundException(Product.class, productSerial);
    }

    Review review = entityFactory.createEntity(Review.class);
    review.setTitle(createReviewDto.getTitle());
    review.setRating(createReviewDto.getRating());
    review.setText(createReviewDto.getText());
    review.setProduct(product);
    review.setUser(user);

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
}
