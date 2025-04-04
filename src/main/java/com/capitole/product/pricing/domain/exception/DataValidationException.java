package com.capitole.product.pricing.domain.exception;

public class DataValidationException extends ProductPricingException {
  public static final String INVALID_PARAM_CODE_ERROR = "missing_param_product_pricing";
  public static final String MESSAGE_ERROR_PARAM_REQUIRED = "El parámetro %s es requerido";
  public static final String MESSAGE_ERROR_INVALID_PARAM = "El parámetro %s es inválido";

  public DataValidationException(String field, String message, String codeError) {
    super(String.format(message, field), codeError);
  }
}
