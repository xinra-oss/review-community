package com.xinra.reviewcommunity.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.xinra.nucleus.entity.EntityPk;
import com.xinra.nucleus.service.ServiceProvider;
import com.xinra.reviewcommunity.SampleMarketSpecificEntity;
import com.xinra.reviewcommunity.entity.SerialEntity;
import java.util.function.Supplier;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
public class TestSerialService {
  
  private @Autowired ServiceProvider serviceProvider;
  private @Autowired PlatformTransactionManager transactionManager;
  
  @Test
  public void topLevelSerial() {
    final SerialService serialService = serviceProvider.getService(SerialService.class);
    final Class<? extends SerialEntity> entityType = SampleMarketSpecificEntity.class;

    test(() -> serialService.getNextSerial(entityType));
  }
  
  @Test
  public void childSerial() {
    final SerialService serialService = serviceProvider.getService(SerialService.class);
    final String name = "child";
    final String entityId = "mock-id";
    final EntityPk entityPk = () -> entityId;
    
    test(() -> serialService.getNextChildSerial(entityPk, name));
  }
  
  private void test(Supplier<Integer> serviceMethod) {
    assertThat(serviceMethod.get())
      .as("create serial and set to 1, if there is none")
      .isEqualTo(1);
  
    assertThat(serviceMethod.get())
      .as("increment serial")
      .isEqualTo(2);
    
    TransactionTemplate transaction = new TransactionTemplate(transactionManager);
    transaction.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
    transaction.execute(new TransactionCallbackWithoutResult() {
      @Override
      protected void doInTransactionWithoutResult(TransactionStatus status) {
        serviceMethod.get(); // 3 is consumed
        status.setRollbackOnly();
      }
    });
    
    assertThat(serviceMethod.get())
      .as("rollback in consuming transaction discards serial")
      .isEqualTo(4);
  }
  
}
