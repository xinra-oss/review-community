package com.xinra.reviewcommunity.rest.conf;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

/**
 * By default, Spring will redirect a failed authentication request to the login URL. For a REST API
 * this is no proper behavior. Instead, this implementation returns a {@code 401 Unauthorized}
 * response.
 */
@Component
public class RestAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
  
  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException exception) throws IOException, ServletException {
    
    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, exception.getMessage());
  }
}
