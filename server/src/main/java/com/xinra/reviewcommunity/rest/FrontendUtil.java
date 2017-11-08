package com.xinra.reviewcommunity.rest;

import com.xinra.nucleus.common.ApplicationContextProvider;
import com.xinra.reviewcommunity.dto.AuthenticatedUserDto;
import com.xinra.reviewcommunity.shared.dto.DtoFactory;
import com.xinra.reviewcommunity.shared.dto.UserDto;
import org.springframework.web.context.request.RequestContextHolder;

public class FrontendUtil {
  
  private FrontendUtil() {}
  
  /**
   * Returns whether the current thread is a web request.
   */
  // TODO: move to nucleus-frontend
  public static boolean isRequest() {
    return RequestContextHolder.getRequestAttributes() != null;
  }
  
  /**
   * Creates a {@link UserDto} from an {@link AuthenticatedUserDto}. This is necessary if the
   * authenticated user should be returned by a REST endpoint. Even if the declared type is
   * {@link UserDto}, all properties would be marshaled. Thus a new DTO is created that only
   * contains the data that may be accessed by the client.
   */
  public static UserDto trimAuthenticatedUser(AuthenticatedUserDto user) {
    UserDto userDto = ApplicationContextProvider.getApplicationContext()
        .getBean(DtoFactory.class).createDto(UserDto.class);
    userDto.setLevel(user.getLevel());
    userDto.setName(user.getName());
    return userDto;
  }
}
