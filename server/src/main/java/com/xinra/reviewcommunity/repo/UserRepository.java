package com.xinra.reviewcommunity.repo;

import com.xinra.nucleus.entity.AbstractEntityRepository;
import com.xinra.reviewcommunity.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository<T extends User> extends AbstractEntityRepository<T> {

  T findByName(String name);
  
}
