package com.xinra.reviewcommunity.repo;

import com.xinra.nucleus.entity.AbstractEntityRepository;
import com.xinra.reviewcommunity.entity.ChildSerial;

public interface ChildSerialRepository<T extends ChildSerial> extends AbstractEntityRepository<T> {
  
  T findByEntityIdAndName(String entityId, String name);
  
}
