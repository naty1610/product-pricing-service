package com.capitole.product.pricing.infrastructure.adapter.controller;

import com.capitole.product.pricing.application.service.PriceService;
import com.capitole.product.pricing.application.handler.request.PriceParamsRequest;
import com.capitole.product.pricing.application.handler.response.PriceResponse;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class PriceController {

    private final PriceService priceService;

    /**
     * Obtiene el precio aplicable para un producto y marca en una fecha dada.
     *
     * @param paramsRequest request con los parametros necesarios para la consulta
     * @return Mono con el precio encontrado
     */
    public Mono<PriceResponse> getApplicablePrice(PriceParamsRequest paramsRequest) {

        return priceService.findApplicablePrice(paramsRequest.date(), paramsRequest.productId(), paramsRequest.brandId());
    }
}
