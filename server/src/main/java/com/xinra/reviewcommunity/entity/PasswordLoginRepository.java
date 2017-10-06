package com.xinra.reviewcommunity.entity;

import com.xinra.nucleus.entity.AbstractEntityRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordLoginRepository<T extends PasswordLogin>
    extends AbstractEntityRepository<T> {

}
