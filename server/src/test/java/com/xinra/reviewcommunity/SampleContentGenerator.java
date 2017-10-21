package com.xinra.reviewcommunity;

import com.xinra.nucleus.common.ContextHolder;
import com.xinra.nucleus.entity.EntityFactory;
import com.xinra.nucleus.service.ServiceProvider;
import com.xinra.reviewcommunity.auth.Role;
import com.xinra.reviewcommunity.entity.Market;
import com.xinra.reviewcommunity.entity.Product;
import com.xinra.reviewcommunity.repo.MarketRepository;
import com.xinra.reviewcommunity.repo.ProductRepository;
import com.xinra.reviewcommunity.service.MarketService;
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
  
  private @Autowired ContextHolder<Context> contextHolder;
  private @Autowired Environment environment;
  private @Autowired ServiceProvider serviceProvider;
  private @Autowired EntityFactory entityFactory;
  private @Autowired MarketRepository<Market> marketRepo;
  @SuppressWarnings("unused")
  private @Autowired ProductRepository<Product> productRepo;
  private @Value("${spring.jpa.hibernate.ddl-auto:''}") String schemaExport;
  
  @Override
  public void onApplicationEvent(ContextRefreshedEvent event) {
    if (environment.acceptsProfiles("test") || schemaExport.startsWith("create")) {
      log.info("Start generating sample data");
      createMarkets();
      if (environment.acceptsProfiles("dev")) {
        createProducts();
        createUsers();
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
    userService.createUserWithPassword("justus", null, "123");
    userService.addRole("justus", Role.ADMIN);
    userService.createUserWithPassword("peter", null, "123");
    userService.addRole("peter", Role.MODERATOR);
    userService.createUserWithPassword("bob", null, "123");
  }
  
  private void createProducts() {
    contextHolder.mock().setMarket(serviceProvider.getService(MarketService.class).getBySlug("de"));
    
    entityFactory.createEntity(Product.class);
    
    contextHolder.clearMock();
  }

}
