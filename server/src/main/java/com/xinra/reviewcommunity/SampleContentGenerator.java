package com.xinra.reviewcommunity;

import com.xinra.nucleus.common.ContextHolder;
import com.xinra.nucleus.entity.EntityFactory;
import com.xinra.nucleus.service.DtoFactory;
import com.xinra.nucleus.service.ServiceProvider;
import com.xinra.reviewcommunity.auth.Role;
import com.xinra.reviewcommunity.dto.AuthenticatedUserDto;
import com.xinra.reviewcommunity.dto.CreateBrandDto;
import com.xinra.reviewcommunity.dto.CreateCategoryDto;
import com.xinra.reviewcommunity.dto.CreateProductDto;
import com.xinra.reviewcommunity.dto.CreateReviewDto;
import com.xinra.reviewcommunity.dto.VoteDto;
import com.xinra.reviewcommunity.entity.Market;
import com.xinra.reviewcommunity.entity.Product;
import com.xinra.reviewcommunity.entity.User;
import com.xinra.reviewcommunity.repo.MarketRepository;
import com.xinra.reviewcommunity.repo.ProductRepository;
import com.xinra.reviewcommunity.repo.UserRepository;
import com.xinra.reviewcommunity.service.AuthenticationProviderImpl;
import com.xinra.reviewcommunity.service.BrandService;
import com.xinra.reviewcommunity.service.CategoryService;
import com.xinra.reviewcommunity.service.MarketService;
import com.xinra.reviewcommunity.service.ProductService;
import com.xinra.reviewcommunity.service.ReviewService;
import com.xinra.reviewcommunity.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Profile({"test", "dev"})
public class SampleContentGenerator implements ApplicationListener<ContextRefreshedEvent> {
  
  private static final String USERNAME_ADMIN = "justus";
  private static final String USERNAME_MODERATOR = "peter";
  private static final String USERNAME_USER = "bob";
  private static final String PASSWORD = "123";
  
  public AuthenticatedUserDto admin;
  public AuthenticatedUserDto moderator;
  public AuthenticatedUserDto user;
  
  private @Autowired ContextHolder<Context> contextHolder;
  private @Autowired Environment environment;
  private @Autowired ServiceProvider serviceProvider;
  private @Autowired DtoFactory dtoFactory;
  private @Autowired EntityFactory entityFactory;
  private @Autowired MarketRepository<Market> marketRepo;
  private @Autowired UserRepository<User> userRepo;
  @SuppressWarnings("unused")
  private @Autowired ProductRepository<Product> productRepo;
  private @Value("${spring.jpa.hibernate.ddl-auto:''}") String schemaExport;

  @Override
  public void onApplicationEvent(ContextRefreshedEvent event) {
    if (environment.acceptsProfiles("test") || schemaExport.startsWith("create")) {
      log.info("Start generating sample data");
      createMarkets();
      if (environment.acceptsProfiles("dev", "test")) {
        createUsers();
        createBrands();
        createCategories();
        createProducts();
//        createReviews();
//        createVotes();
      }
      log.info("Finished generating sample data");
    }
  }
  
  private void createMarkets() {
    Market m1 = entityFactory.createEntity(Market.class);
    m1.setSlug("de");
    marketRepo.save(m1);
    Market m2 = entityFactory.createEntity(Market.class);
    m2.setSlug("us");
    marketRepo.save(m2);
    
    serviceProvider.getService(MarketService.class).buildCache();
  }
  
  private void createUsers() {
    UserService userService = serviceProvider.getService(UserService.class);
    
    // create admin
    userService.createUserWithPassword(USERNAME_ADMIN, null, PASSWORD);
    userService.addRole(USERNAME_ADMIN, Role.ADMIN);
    admin = AuthenticationProviderImpl
        .getAuthenticatedUserDto(userRepo.findByNameFetchRoles(USERNAME_ADMIN), dtoFactory);
    
    // create moderator
    userService.createUserWithPassword(USERNAME_MODERATOR, null, PASSWORD);
    userService.addRole(USERNAME_MODERATOR, Role.MODERATOR);
    moderator = AuthenticationProviderImpl
        .getAuthenticatedUserDto(userRepo.findByNameFetchRoles(USERNAME_MODERATOR), dtoFactory);
    
    // create normal user
    userService.createUserWithPassword(USERNAME_USER, null, PASSWORD);
    user = AuthenticationProviderImpl
        .getAuthenticatedUserDto(userRepo.findByNameFetchRoles(USERNAME_USER), dtoFactory);
  }

  private void createCategories() {
    contextHolder.mock().setMarket(serviceProvider.getService(MarketService.class).getBySlug("de"));

    CategoryService categoryService = serviceProvider.getService(CategoryService.class);

    CreateCategoryDto createCategoryDto1 = dtoFactory.createDto(CreateCategoryDto.class);
    createCategoryDto1.setName("Lebensmittel");
    createCategoryDto1.setParentSerial(0);
    categoryService.createCategory(createCategoryDto1);

    CreateCategoryDto createCategoryDto2 = dtoFactory.createDto(CreateCategoryDto.class);
    createCategoryDto2.setName("Süßigkeiten");
    createCategoryDto2.setParentSerial(1);
    categoryService.createCategory(createCategoryDto2);

    CreateCategoryDto createCategoryDto3 = dtoFactory.createDto(CreateCategoryDto.class);
    createCategoryDto3.setName("Fruchgummi");
    createCategoryDto3.setParentSerial(2);
    categoryService.createCategory(createCategoryDto3);

    CreateCategoryDto createCategoryDto4 = dtoFactory.createDto(CreateCategoryDto.class);
    createCategoryDto4.setName("Getränke");
    createCategoryDto4.setParentSerial(1);
    categoryService.createCategory(createCategoryDto4);

    CreateCategoryDto createCategoryDto5 = dtoFactory.createDto(CreateCategoryDto.class);
    createCategoryDto5.setName("Softdrinks");
    createCategoryDto5.setParentSerial(4);
    categoryService.createCategory(createCategoryDto5);

    contextHolder.clearMock();
  }

  private void createBrands() {
    contextHolder.mock().setMarket(serviceProvider.getService(MarketService.class).getBySlug("de"));

    BrandService brandService = serviceProvider.getService(BrandService.class);

    CreateBrandDto createBrandDto1 = dtoFactory.createDto(CreateBrandDto.class);
    createBrandDto1.setName("Haribo");
    brandService.createBrand(createBrandDto1);

    CreateBrandDto createBrandDto2 = dtoFactory.createDto(CreateBrandDto.class);
    createBrandDto2.setName("Coca Cola");
    brandService.createBrand(createBrandDto2);

    contextHolder.clearMock();
  }

  private void createProducts() {
    contextHolder.mock().setMarket(serviceProvider.getService(MarketService.class).getBySlug("de"));

    CreateProductDto createProductDto1 = dtoFactory.createDto(CreateProductDto.class);
    createProductDto1.setName("Color-Rado");
    createProductDto1.setDescription("They're sweet.");
    createProductDto1.setBrandSerial(1);
    createProductDto1.setCategorySerial(3);

    CreateProductDto createProductDto2 = dtoFactory.createDto(CreateProductDto.class);
    createProductDto2.setName("Goldbären");
    createProductDto2.setDescription("They're golden.");
    createProductDto2.setBrandSerial(1);
    createProductDto2.setCategorySerial(3);

    CreateProductDto createProductDto3 = dtoFactory.createDto(CreateProductDto.class);
    createProductDto3.setName("No Man's Sky");
    createProductDto3.setDescription("Idle Game");
    createProductDto3.setBrandSerial(2);
    createProductDto3.setCategorySerial(5);

    CreateProductDto createProductDto4 = dtoFactory.createDto(CreateProductDto.class);
    createProductDto4.setName("Coca Cola Zero");
    createProductDto4.setDescription("Echter Geschmack - Zero Zucker");
    createProductDto4.setBrandSerial(2);
    createProductDto4.setCategorySerial(5);

    CreateProductDto createProductDto5 = dtoFactory.createDto(CreateProductDto.class);
    createProductDto5.setName("Coca Cola ");
    createProductDto5.setDescription("Timeless taste");
    createProductDto5.setBrandSerial(2);
    createProductDto5.setCategorySerial(5);

    ProductService productService = serviceProvider.getService(ProductService.class);
    productService.createProduct(createProductDto1);
    productService.createProduct(createProductDto2);
    productService.createProduct(createProductDto3);
    productService.createProduct(createProductDto4);
    productService.createProduct(createProductDto5);

    contextHolder.clearMock();
  }

  private void createReviews() {
    contextHolder.mock().setMarket(serviceProvider.getService(MarketService.class).getBySlug("de"));
//    contextHolder.get().setAuthenticatedUser(serviceProvider.getService(UserService.class)
//            .createUserWithPassword("Eric","eric@coon.org", "coonandfriends" ));
//
//    AuthenticatedUserDto authenticatedUserDto = dtoFactory.createDto(AuthenticatedUserDto.class);
//
//
//    contextHolder.get().setAuthenticatedUser();


    ReviewService reviewService = serviceProvider.getService(ReviewService.class);

    CreateReviewDto createReviewDto1 = dtoFactory.createDto(CreateReviewDto.class);
    createReviewDto1.setTitle("My review on PUBG");
    createReviewDto1.setText("FPP ftw!");
    createReviewDto1.setRating(1);
    reviewService.createReview(createReviewDto1, 3);

    CreateReviewDto createReviewDto2 = dtoFactory.createDto(CreateReviewDto.class);
    createReviewDto2.setTitle("My Review on PUBG");
    createReviewDto2.setText("E-Sports ready");
    createReviewDto2.setRating(2);
    reviewService.createReview(createReviewDto2, 3);

    contextHolder.clearMock();
  }

  private void createVotes() {
    contextHolder.mock().setMarket(serviceProvider.getService(MarketService.class).getBySlug("de"));

    ReviewService reviewService = serviceProvider.getService(ReviewService.class);

    VoteDto voteDto1 = dtoFactory.createDto(VoteDto.class);
    voteDto1.setUpvote(true);
    reviewService.vote(voteDto1, 1);

    VoteDto voteDto2 = dtoFactory.createDto(VoteDto.class);
    voteDto2.setUpvote(true);
    reviewService.vote(voteDto2, 1);

    VoteDto voteDto3 = dtoFactory.createDto(VoteDto.class);
    voteDto3.setUpvote(false);
    reviewService.vote(voteDto3, 1);

    VoteDto voteDto4 = dtoFactory.createDto(VoteDto.class);
    voteDto4.setUpvote(true);
    reviewService.vote(voteDto4, 2);

    VoteDto voteDto5 = dtoFactory.createDto(VoteDto.class);
    voteDto5.setUpvote(false);
    reviewService.vote(voteDto5, 2);
  }
}
