package com.xinra.reviewcommunity.repo;

import com.xinra.reviewcommunity.entity.PasswordLogin;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordLoginRepository<T extends PasswordLogin> extends LoginRepository<T> {

}
