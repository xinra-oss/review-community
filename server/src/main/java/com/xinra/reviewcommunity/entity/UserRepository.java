package com.xinra.reviewcommunity.entity;

import com.xinra.nucleus.entity.AbstractEntityRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository<T extends User> extends AbstractEntityRepository<T> {

  T getByName(String name);
  
}
