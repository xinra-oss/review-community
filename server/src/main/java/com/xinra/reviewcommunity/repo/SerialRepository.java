package com.xinra.reviewcommunity.repo;

import com.xinra.nucleus.entity.AbstractEntityRepository;
import com.xinra.reviewcommunity.entity.Serial;

public interface SerialRepository<T extends Serial> extends AbstractEntityRepository<T> {
  
  T findByName(String name);
  
}
