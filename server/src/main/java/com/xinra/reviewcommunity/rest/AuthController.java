package com.xinra.reviewcommunity.rest;

import com.xinra.reviewcommunity.rest.conf.MarketAgnostic;
import com.xinra.reviewcommunity.shared.dto.CsrfTokenDto;

import javax.servlet.http.HttpServletRequest;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@MarketAgnostic
public class AuthController extends AbstractController {

  /**
   * Get a CSRF token.
   */
  @RequestMapping(path = "/csrf-token", method = RequestMethod.GET)
  public CsrfTokenDto getCsrfToken(HttpServletRequest request) {
    CsrfToken token = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
    CsrfTokenDto dto = dtoFactory.createDto(CsrfTokenDto.class);
    dto.setHeaderName(token.getHeaderName());
    dto.setParameterName(token.getParameterName());
    dto.setToken(token.getToken());
    return dto;
  }
  
}
