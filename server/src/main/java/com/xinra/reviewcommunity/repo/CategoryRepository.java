package com.xinra.reviewcommunity.repo;

import com.xinra.nucleus.entity.AbstractEntityRepository;
import com.xinra.reviewcommunity.entity.Category;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository<T extends Category> extends AbstractEntityRepository<T> {


}
