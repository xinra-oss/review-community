package com.xinra.reviewcommunity.rest;

import com.xinra.nucleus.common.ContextHolder;
import com.xinra.reviewcommunity.Context;
import com.xinra.reviewcommunity.dto.AuthenticatedUserDto;
import com.xinra.reviewcommunity.shared.dto.InitDto;
import com.xinra.reviewcommunity.shared.dto.UserDto;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InitController extends AbstractController {
  
  private @Autowired ContextHolder<Context> contextHolder;
  private @Autowired AuthController authController;
  private @Autowired MarketController marketController;
  private @Autowired CategoryController categoryController;

  /**
   * GET information required to initialize the client.
   */
  @RequestMapping(path = "/init", method = RequestMethod.GET)
  public InitDto init(HttpServletRequest request) {
    InitDto initDto = dtoFactory.createDto(InitDto.class);
    
    UserDto authenticatedUser = contextHolder.get().getAuthenticatedUser()
        .map(FrontendUtil::trimAuthenticatedUser).orElse(null);
    
    initDto.setCsrfToken(authController.getCsrfToken(request));
    initDto.setAuthenticatedUser(authenticatedUser);
    initDto.setPermissions(contextHolder.get().getAuthenticatedUser()
        .map(AuthenticatedUserDto::getPermissions).orElse(null));
    initDto.setMarkets(marketController.getList());
    initDto.setCategoryTree(categoryController.getList());
    
    return initDto;
  }
  
}
