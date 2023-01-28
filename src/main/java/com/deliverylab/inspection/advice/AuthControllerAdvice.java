package com.deliverylab.inspection.advice;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.deliverylab.inspection.exception.ErrorMessage;
import com.deliverylab.inspection.exception.RoleNotFoundException;
import com.deliverylab.inspection.exception.TokenRefreshException;

@RestControllerAdvice
public class AuthControllerAdvice {

  @ExceptionHandler(value = TokenRefreshException.class)
  @ResponseStatus(HttpStatus.FORBIDDEN)
  public ErrorMessage handleTokenRefreshException(TokenRefreshException ex, WebRequest request) {
    return new ErrorMessage(
        HttpStatus.FORBIDDEN.value(),
        new Date(),
        ex.getMessage(),
        request.getDescription(false));
  }

  @ExceptionHandler(value = RoleNotFoundException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorMessage handleTokenRefreshException(RoleNotFoundException ex, WebRequest request) {
    return new ErrorMessage(
        HttpStatus.BAD_REQUEST.value(),
        new Date(),
        ex.getMessage(),
        request.getDescription(false));
  }
}