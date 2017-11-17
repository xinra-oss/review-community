package com.xinra.reviewcommunity.rest;

import com.xinra.reviewcommunity.Util;
import com.xinra.reviewcommunity.rest.conf.MarketAgnostic;
import com.xinra.reviewcommunity.service.UserService;
import com.xinra.reviewcommunity.service.UserService.UsernameAlreadyExistsException;
import com.xinra.reviewcommunity.shared.dto.RegistrationDto;

import javax.validation.Valid;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController extends AbstractController {

  /**
   * Register a new user.
   */
  @MarketAgnostic
  @RequestMapping(path = "", method = RequestMethod.POST)
  public void register(@Valid @RequestBody RegistrationDto registerDto,
      BindingResult result) throws BindException {
    
    if (result.hasErrors()) {
      throw new BindException(result);
    }
    try {
      serviceProvider.getService(UserService.class).createUserWithPassword(
          registerDto.getUsername(),
          Util.normalize(registerDto.getEmail()),
          registerDto.getPassword());
    } catch (UsernameAlreadyExistsException ex) {
      result.rejectValue("username", "AlreadyExists");
      throw new BindException(result);
    }
  }
  
}
