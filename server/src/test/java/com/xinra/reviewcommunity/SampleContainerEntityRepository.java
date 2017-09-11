package com.xinra.reviewcommunity;

import com.xinra.nucleus.entity.AbstractEntityRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Repository
@Profile("test")
public interface SampleContainerEntityRepository 
    extends AbstractEntityRepository<SampleContainerEntity> {

}
