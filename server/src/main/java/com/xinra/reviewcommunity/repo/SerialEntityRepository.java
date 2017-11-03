package com.xinra.reviewcommunity.repo;

import com.xinra.nucleus.entity.AbstractEntityRepository;
import com.xinra.reviewcommunity.entity.SerialEntity;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface SerialEntityRepository<T extends SerialEntity>
        extends AbstractEntityRepository<T> {

  T findBySerial(int serial);

}
