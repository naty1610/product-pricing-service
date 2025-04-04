package com.capitole.product.pricing.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Price(Long productId, Long brandId, Long priceList,
                    LocalDateTime startDate, LocalDateTime endDate,
                    BigDecimal price) {
}

