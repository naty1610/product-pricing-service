package com.capitole.product.pricing.infrastructure.adapter.controller;

import com.capitole.product.pricing.application.handler.request.PriceParamsRequest;
import com.capitole.product.pricing.application.handler.response.PriceResponse;
import com.capitole.product.pricing.application.service.PriceService;
import com.capitole.product.pricing.domain.exception.ProductPricingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.Mockito.*;

public class PriceControllerTest {
    private PriceService priceService;
    private PriceController priceController;

    @BeforeEach
    public void setUp() {
        priceService = mock(PriceService.class);
        priceController = new PriceController(priceService);
    }

    @Test
    public void testGetApplicablePrice_Success() {
        LocalDateTime date = LocalDateTime.of(2020, 6, 14, 10, 0);
        Long productId = 35455L;
        Long brandId = 1L;

        PriceParamsRequest request = new PriceParamsRequest(brandId, date, productId);

        PriceResponse response = new PriceResponse(productId, brandId, 2L, date, date.plusHours(2), BigDecimal.valueOf(35.50));

        when(priceService.findApplicablePrice(date, productId, brandId)).thenReturn(Mono.just(response));

        StepVerifier.create(priceController.getApplicablePrice(request))
                .expectNext(response)
                .verifyComplete();

        verify(priceService).findApplicablePrice(date, productId, brandId);
    }

    @Test
    public void testGetApplicablePrice_NotFound() {
        LocalDateTime date = LocalDateTime.of(2020, 6, 14, 10, 0);
        Long productId = 35455L;
        Long brandId = 1L;

        PriceParamsRequest request = new PriceParamsRequest(brandId, date, productId);

        when(priceService.findApplicablePrice(date, productId, brandId)).thenReturn(Mono.empty());

        StepVerifier.create(priceController.getApplicablePrice(request))
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    public void testGetApplicablePrice_Error() {
        LocalDateTime date = LocalDateTime.of(2020, 6, 14, 10, 0);
        Long productId = 35455L;
        Long brandId = 1L;

        PriceParamsRequest request = new PriceParamsRequest(brandId, date, productId);

        String errorMessage = "Ocurrió un error inesperado";
        when(priceService.findApplicablePrice(date, productId, brandId)).thenReturn(Mono.error(new RuntimeException(errorMessage) ));

        StepVerifier.create(priceController.getApplicablePrice(request))
                .expectErrorMatches(throwable ->
                        throwable instanceof ProductPricingException &&
                                throwable.getMessage().equals(errorMessage));
    }
}
