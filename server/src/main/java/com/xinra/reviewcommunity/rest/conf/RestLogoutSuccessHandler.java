package com.xinra.reviewcommunity.rest.conf;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

/**
 * By default, Spring redirects successful logout requests to the login URL. For a REST API this is
 * no proper behavior. Instead, this implementation returns a {@code 200 OK} response.
 */
@Component
public class RestLogoutSuccessHandler implements LogoutSuccessHandler {

  @Override
  public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException, ServletException {
    
    response.setStatus(HttpServletResponse.SC_OK);
    response.getWriter().flush();
  }

}
