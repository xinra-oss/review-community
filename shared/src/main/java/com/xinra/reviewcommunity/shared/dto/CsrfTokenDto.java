package com.xinra.reviewcommunity.shared.dto;

import lombok.Data;
import lombok.NonNull;

/**
 * Copy of {@code org.springframework.security.web.csrf.CsrfToken} to avoid dependency.
 */
@Data
public class CsrfTokenDto implements Dto {

  private @NonNull String headerName;
  private @NonNull String parameterName;
  private @NonNull String token;

}
