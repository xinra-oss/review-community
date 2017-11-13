package com.xinra.reviewcommunity.shared.dto;

import lombok.Data;

/**
 * Copy of {@code org.springframework.security.web.csrf.CsrfToken} to avoid dependency.
 */
@Data
public class CsrfTokenDto implements Dto {

  private String headerName;
  private String parameterName;
  private String token;

}
