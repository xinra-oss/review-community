package com.xinra.reviewcommunity.rest;

import com.xinra.nucleus.service.ServiceProvider;
import com.xinra.reviewcommunity.shared.dto.DtoFactory;

import org.springframework.beans.factory.annotation.Autowired;

public class AbstractController {
  
  protected @Autowired DtoFactory dtoFactory;
  protected @Autowired ServiceProvider serviceProvider;

}
