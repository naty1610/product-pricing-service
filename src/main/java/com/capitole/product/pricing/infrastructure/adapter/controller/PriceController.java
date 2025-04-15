package com.capitole.product.pricing.infrastructure.adapter.controller;

import com.capitole.product.pricing.application.service.PriceService;
import com.capitole.product.pricing.application.handler.request.PriceParamsRequest;
import com.capitole.product.pricing.application.handler.response.PriceResponse;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class PriceController {

    private final PriceService priceService;

    public Mono<PriceResponse> getApplicablePrice(PriceParamsRequest paramsRequest) {

        return priceService.findApplicablePrice(paramsRequest.date(), paramsRequest.productId(), paramsRequest.brandId());
    }
}
