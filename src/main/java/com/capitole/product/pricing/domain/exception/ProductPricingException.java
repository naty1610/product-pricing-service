package com.capitole.product.pricing.domain.exception;

import lombok.Getter;

@Getter
public class ProductPricingException extends Exception {
  private final String codeError;

  public ProductPricingException(String message, String codeError){
    super(message);
    this.codeError = codeError;
  }

  public ProductPricingException(String message, String codeError, Throwable cause){
    super(message, cause);
    this.codeError = codeError;
  }
}
