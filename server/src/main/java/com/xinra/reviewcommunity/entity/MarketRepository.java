package com.xinra.reviewcommunity.entity;

import com.xinra.nucleus.entity.AbstractEntityRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarketRepository<T extends Market> extends AbstractEntityRepository<T> {

}
