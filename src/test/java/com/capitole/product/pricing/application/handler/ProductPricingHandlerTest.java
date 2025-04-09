package com.capitole.product.pricing.application.handler;

import com.capitole.product.pricing.application.handler.request.PriceParamsRequest;
import com.capitole.product.pricing.application.handler.response.PriceResponse;
import com.capitole.product.pricing.infrastructure.adapter.controller.PriceController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductPricingHandlerTest {

    @Mock
    private PriceController priceController;

    @InjectMocks
    private ProductPricingHandler productPricingHandler;

    @Test
    void testHandleRequest_Success() {
        LocalDateTime date = LocalDateTime.of(2020, 6, 14, 10, 0);
        Long productId = 35455L;
        Long brandId = 1L;

        ServerRequest request = mock(ServerRequest.class);
        when(request.queryParam("brand_id")).thenReturn(java.util.Optional.of(brandId.toString()));
        when(request.queryParam("product_id")).thenReturn(java.util.Optional.of(productId.toString()));
        when(request.queryParam("date")).thenReturn(java.util.Optional.of(date.toString()));

        PriceResponse mockResponse = new PriceResponse(productId, brandId, 2L, date, date.plusHours(2), BigDecimal.valueOf(35.50));
        when(priceController.getApplicablePrice(any(PriceParamsRequest.class)))
                .thenReturn(Mono.just(mockResponse));

        Mono<ServerResponse> response = productPricingHandler.handleRequest(request);

        StepVerifier.create(response)
                .expectNextMatches(serverResponse ->
                        serverResponse.statusCode().is2xxSuccessful())
                .verifyComplete();
    }

    @Test
    void testHandleRequest_ReturnBadRequestWhenMissingParams() {
        ServerRequest request = mock(ServerRequest.class);
        when(request.queryParam("brand_id")).thenReturn(java.util.Optional.empty());

        Mono<ServerResponse> response = productPricingHandler.handleRequest(request);

        StepVerifier.create(response)
                .expectNextMatches(res -> res.statusCode().is4xxClientError())
                .verifyComplete();
    }

    @Test
    void testHandleRequest_ReturnBadRequestWhenInvalidNumber() {
        ServerRequest request = mock(ServerRequest.class);
        when(request.queryParam("brand_id")).thenReturn(java.util.Optional.of("invalid"));
        when(request.queryParam("product_id")).thenReturn(java.util.Optional.of("35455"));
        when(request.queryParam("date")).thenReturn(java.util.Optional.of("2020-06-14T10:00:00"));

        Mono<ServerResponse> response = productPricingHandler.handleRequest(request);

        StepVerifier.create(response)
                .expectNextMatches(res -> res.statusCode().is4xxClientError())
                .verifyComplete();
    }

    @Test
    void testHandleRequest_ReturnBadRequestWhenInvalidDate() {
        ServerRequest request = mock(ServerRequest.class);
        when(request.queryParam("brand_id")).thenReturn(java.util.Optional.of("1"));
        when(request.queryParam("product_id")).thenReturn(java.util.Optional.of("35455"));
        when(request.queryParam("date")).thenReturn(java.util.Optional.of("bad-date"));

        Mono<ServerResponse> response = productPricingHandler.handleRequest(request);

        StepVerifier.create(response)
                .expectNextMatches(res -> res.statusCode().is4xxClientError())
                .verifyComplete();
    }

    @Test
    void testHandleRequest_ControllerError() {
        LocalDateTime date = LocalDateTime.of(2020, 6, 14, 10, 0);
        Long productId = 35455L;
        Long brandId = 1L;

        ServerRequest request = mock(ServerRequest.class);
        when(request.queryParam("brand_id")).thenReturn(java.util.Optional.of(Long.toString(brandId)));
        when(request.queryParam("product_id")).thenReturn(java.util.Optional.of(Long.toString(productId)));
        when(request.queryParam("date")).thenReturn(java.util.Optional.of(date.toString()));

        when(priceController.getApplicablePrice(any(PriceParamsRequest.class)))
                .thenReturn(Mono.error(new RuntimeException("Error")));

        Mono<ServerResponse> response = productPricingHandler.handleRequest(request);

        StepVerifier.create(response)
                .expectNextMatches(res -> res.statusCode().is5xxServerError())
                .verifyComplete();
    }

}
