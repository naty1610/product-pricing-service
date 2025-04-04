package com.capitole.product.pricing.application.handler.request;

import java.time.LocalDateTime;

public record PriceParamsRequest(Long brandId, LocalDateTime date, Long productId) {
}
