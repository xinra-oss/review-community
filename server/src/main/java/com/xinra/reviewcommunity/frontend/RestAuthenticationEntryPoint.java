package com.xinra.reviewcommunity.frontend;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * By default, Spring will redirect a request that requires authentication to the login URL. For a
 * REST API this is no proper behavior. Instead, this implementation returns a
 * {@code 401 Unauthorized} response.
 */
@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException authException) throws IOException, ServletException {
    
    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
  }
}
