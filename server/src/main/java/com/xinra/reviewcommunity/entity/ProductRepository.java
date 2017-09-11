package com.xinra.reviewcommunity.entity;

import com.xinra.nucleus.entity.AbstractEntityRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository<T extends Product> extends AbstractEntityRepository<T> {

}
