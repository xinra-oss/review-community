package com.xinra.reviewcommunity.repo;

import com.xinra.reviewcommunity.entity.Category;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository<T extends Category> extends SerialEntityRepository<T> {

}
