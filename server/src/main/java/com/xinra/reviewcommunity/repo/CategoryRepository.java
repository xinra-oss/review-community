package com.xinra.reviewcommunity.repo;

import com.xinra.reviewcommunity.entity.Category;
import java.util.Set;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository<T extends Category> extends SerialEntityRepository<T> {

  Set<T> findByParentIsNull();
}
