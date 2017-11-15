package com.xinra.reviewcommunity.shared;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiException extends RuntimeException {

  private long timestamp;
  private int status;
  private String error;
  private String exception;
  private String message;
  private String path;

  @Override
  public String getMessage() {
    return message;
  }
}
