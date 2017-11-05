package com.xinra.reviewcommunity.service;

import com.xinra.nucleus.entity.EntityFactory;
import com.xinra.nucleus.service.ServiceImpl;
import com.xinra.nucleus.service.ServiceProvider;
import com.xinra.reviewcommunity.shared.dto.DtoFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public abstract class AbstractService extends ServiceImpl {
  
  protected @Autowired EntityFactory entityFactory;
  protected @Autowired DtoFactory dtoFactory;
  protected @Autowired ServiceProvider serviceProvider;

}
