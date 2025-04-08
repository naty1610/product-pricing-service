package com.capitole.product.pricing.domain.repository;

import com.capitole.product.pricing.domain.model.Price;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

public interface PriceRepository {
    Mono<Price> findApplicablePrice(LocalDateTime date, Long productId, Long brandId);
}