package com.deliverylab.inspection.payload.request.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidRequest {
  @NotBlank
  private String accessToken;
}