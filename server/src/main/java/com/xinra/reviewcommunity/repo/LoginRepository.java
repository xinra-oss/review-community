package com.xinra.reviewcommunity.repo;

import com.xinra.nucleus.entity.AbstractEntityRepository;
import com.xinra.reviewcommunity.entity.Login;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

@NoRepositoryBean
public interface LoginRepository<T extends Login> extends AbstractEntityRepository<T> {
  
  @Query("SELECT l FROM Login l JOIN FETCH l.user.roles WHERE l.user.name = :username")
  T findByUserNameEager(@Param("username") String username);
  
}
