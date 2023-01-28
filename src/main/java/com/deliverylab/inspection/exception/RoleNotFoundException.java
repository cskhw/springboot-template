package com.deliverylab.inspection.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RoleNotFoundException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public RoleNotFoundException(String message) {
    super(String.format("Error: Role is not found."));
  }
}