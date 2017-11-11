package com.xinra.reviewcommunity.repo;

import com.xinra.nucleus.entity.AbstractEntityRepository;
import com.xinra.reviewcommunity.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository<T extends User> extends AbstractEntityRepository<T> {

  T findByName(String name);
  
  @Query("SELECT u FROM User u LEFT JOIN FETCH u.roles WHERE u.name = :name")
  T findByNameFetchRoles(@Param("name") String name);
  
}
