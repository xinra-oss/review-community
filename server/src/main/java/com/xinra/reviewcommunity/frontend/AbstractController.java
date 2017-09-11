package com.xinra.reviewcommunity.frontend;

import com.xinra.nucleus.service.DtoFactory;
import com.xinra.nucleus.service.ServiceProvider;
import org.springframework.beans.factory.annotation.Autowired;

public class AbstractController {
  
  protected @Autowired DtoFactory dtoFactory;
  protected @Autowired ServiceProvider serviceProvider;

}
