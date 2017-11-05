package com.xinra.reviewcommunity.rest.conf;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xinra.reviewcommunity.dto.AuthenticatedUserDto;
import com.xinra.reviewcommunity.rest.AuthController;
import com.xinra.reviewcommunity.shared.dto.DtoFactory;
import com.xinra.reviewcommunity.shared.dto.SuccessfulAuthenticationDto;
import com.xinra.reviewcommunity.shared.dto.UserDto;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Component;

/**
 * By default, Spring redirects successful authentication requests to another URL. For a REST API
 * this is no proper behavior. Instead, this implementation returns a {@code 200 OK} response along
 * with information about the authenticated user.
 */
@Slf4j
@Component
public class RestAuthenticationSuccessHandler 
    extends SavedRequestAwareAuthenticationSuccessHandler {

  private @Autowired DtoFactory dtoFactory;
  private @Autowired AuthController authController;
  private final ObjectMapper mapper;
  
  @Autowired
  public RestAuthenticationSuccessHandler(MappingJackson2HttpMessageConverter messageConverter) {
    mapper = messageConverter.getObjectMapper();
  }
  
  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws ServletException, IOException {
    
    response.setStatus(HttpServletResponse.SC_OK);
    response.setContentType("application/json");
    
    AuthenticatedUserDto user = (AuthenticatedUserDto) authentication.getPrincipal();
    
    // we have to create a new object so that not all properties are marshaled
    UserDto userDto = dtoFactory.createDto(UserDto.class);
    userDto.setLevel(user.getLevel());
    userDto.setName(user.getName());
    
    SuccessfulAuthenticationDto authDto = dtoFactory.createDto(SuccessfulAuthenticationDto.class);
    authDto.setUser(userDto);
    authDto.setPermissions(user.getPermissions());
    authDto.setCsrfToken(authController.getCsrfToken(request));
    
    log.info("User with name '{}' authenticated", user.getName());
    
    PrintWriter writer = response.getWriter();
    mapper.writeValue(writer, authDto);
    writer.flush();
  }
  
}
