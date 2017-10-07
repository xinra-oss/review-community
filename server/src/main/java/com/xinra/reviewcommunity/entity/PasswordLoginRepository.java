package com.xinra.reviewcommunity.entity;

import org.springframework.stereotype.Repository;

@Repository
public interface PasswordLoginRepository<T extends PasswordLogin> extends LoginRepository<T> {

}
