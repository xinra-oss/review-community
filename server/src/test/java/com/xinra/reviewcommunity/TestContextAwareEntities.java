package com.xinra.reviewcommunity;

import static org.assertj.core.api.Assertions.assertThat;

import com.xinra.nucleus.entity.EntityFactory;
import com.xinra.nucleus.service.ServiceProvider;
import com.xinra.reviewcommunity.service.MarketService;
import javax.transaction.Transactional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
public class TestContextAwareEntities {

  @Autowired EntityFactory entityFactory;
  @Autowired SampleMarketSpecificEntityRepository repo;
  @Autowired SampleContainerEntityRepository containerRepo;
  @Autowired Context context;
  @Autowired ServiceProvider serviceProvider;
  
  @Test
  public void createAndFilterMarketSpecificEntities() {
    MarketService marketService = serviceProvider.getService(MarketService.class);
    
    // market property should be set automatically according to context
    context.setMarket(marketService.getBySlug("de"));
    SampleMarketSpecificEntity entityDe = 
         entityFactory.createEntity(SampleMarketSpecificEntity.class);
    entityDe.setSerial(1);
    assertThat(entityDe.getMarket().getSlug())
        .as("set current market automatically")
        .isEqualTo("de");
    entityDe.setSerial(1);
    entityDe = repo.save(entityDe);
    
    context.setMarket(marketService.getBySlug("us"));
    SampleMarketSpecificEntity entityUs = 
        entityFactory.createEntity(SampleMarketSpecificEntity.class);
    entityUs.setSerial(2);
    entityUs = repo.save(entityUs);
    
    // querying should only return return entities of the context's market
    assertThat(repo.findAll())
        .as("get all entities from current market")
        .containsOnly(entityUs);
    
    // querying should return everything if no market is set in context
    context.setMarket(null);
    assertThat(repo.count())
        .as("get all entities (market-agnostic)")
        .isEqualTo(2);
    
    // TODO decide how collections of market specific entities should behave
    // and implement tests accordingly
    // one-to-many should contain everything if no market is set in context
    //    SampleContainerEntity container = entityFactory.createEntity(SampleContainerEntity.class);
    //    container.setItems(ImmutableSet.of(entityDe, entityUs));
    //    container = containerRepo.save(container);
    //    
    //    container = containerRepo.findAll().iterator().next();
    //    assertThat(container.getItems())
    //        .hasSize(2);
    //    
    //    context.setMarket(marketService.getBySlug("de"));
    //    container = containerRepo.findAll().iterator().next();
    //    assertThat(container.getItems())
    //        .hasSize(1);
    
    repo.deleteAll();
  }
  
}
