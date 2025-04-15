package com.capitole.product.pricing.domain.exception;

public class NotFoundNoSQLServiceException extends ProductPricingException {
    public static final String NOT_FOUND_DATA_CODE_ERROR = "find_product_pricing_error";

    public NotFoundNoSQLServiceException(String message, String codeError) {
        super(message, codeError);
    }
}
