package com.xinra.reviewcommunity.rest.conf;

import com.xinra.reviewcommunity.service.BarcodeService.BarcodeNotFoundException;
import com.xinra.reviewcommunity.service.BarcodeService.ProductOutOfScopeException;
import com.xinra.reviewcommunity.service.MarketService.MarketNotFoundException;
import com.xinra.reviewcommunity.service.SerialNotFoundException;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Global exception handler for the REST API (is applied to all controllers).
 */
@ControllerAdvice
public class ExceptionHandlerAdvice {
  
  @ExceptionHandler({
      SerialNotFoundException.class,
      BarcodeNotFoundException.class,
      ProductOutOfScopeException.class,
      MarketNotFoundException.class
  })
  public void handleNotFoundException(Exception ex, HttpServletResponse response)
      throws IOException {
    response.sendError(HttpServletResponse.SC_NOT_FOUND, ex.getMessage());
  }

}
