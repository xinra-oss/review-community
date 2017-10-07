package com.xinra.reviewcommunity.rest.conf;

import com.fasterxml.jackson.databind.ObjectMapper;
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

  private final ObjectMapper mapper;
  
  @Autowired
  public RestAuthenticationSuccessHandler(MappingJackson2HttpMessageConverter messageConverter) {
    mapper = messageConverter.getObjectMapper();
  }
  
  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws ServletException, IOException {
    
    response.setStatus(HttpServletResponse.SC_OK);
    
    // TODO return user data
    
    log.info("User with name {} authenticated", "erik");
    
    PrintWriter writer = response.getWriter();
    writer.println("{\"foo\": 5}");
    writer.flush();
  }
  
}
