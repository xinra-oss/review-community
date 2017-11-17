package com.xinra.reviewcommunity.repo;

import com.xinra.nucleus.entity.AbstractEntityRepository;
import com.xinra.reviewcommunity.entity.Category;
import java.util.Set;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository<T extends Category> extends AbstractEntityRepository<T> {

  T findBySerial(int serial);

  Set<T> findByParentIsNull();
  
}
