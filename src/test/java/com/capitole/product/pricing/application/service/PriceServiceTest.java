package com.capitole.product.pricing.application.service;

import com.capitole.product.pricing.domain.model.Price;
import com.capitole.product.pricing.domain.repository.PriceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PriceServiceTest {
    @Mock private PriceRepository priceRepository;

    @InjectMocks private PriceService priceService;

    @Test
    public void testFindApplicablePrice_Success(){
        LocalDateTime date = LocalDateTime.of(2020, 6, 14, 10, 0);
        Long productId = 35455L;
        Long brandId = 1L;
        Price expectedPrice = new Price(
                productId, brandId, 2L, date, date.plusHours(3), BigDecimal.valueOf(25.45));

        when(priceRepository.findApplicablePrice(date, productId, brandId)).thenReturn(Mono.just(expectedPrice));

        StepVerifier.create(priceService.findApplicablePrice(date, productId, brandId))
                .assertNext(priceResponse -> {
                    assertEquals(productId, priceResponse.productId());
                    assertEquals(brandId, priceResponse.brandId());
                    assertEquals(2L, priceResponse.priceList());
                    assertEquals(date, priceResponse.startDate());
                    assertEquals(date.plusHours(3), priceResponse.endDate());
                    assertEquals(BigDecimal.valueOf(25.45), priceResponse.price());
                }).verifyComplete();
    }

    @Test
    public void testFindApplicablePrice_NotFound() {
        LocalDateTime date = LocalDateTime.of(2020, 6, 14, 10, 0);
        Long productId = 35455L;
        Long brandId = 1L;

        when(priceRepository.findApplicablePrice(date, productId, brandId)).thenReturn(Mono.empty());

        StepVerifier.create(priceService.findApplicablePrice(date, productId, brandId))
                .expectError(ResponseStatusException.class)
                .verify();
    }

    @Test
    public void testFindApplicablePrice_Error() {
        LocalDateTime date = LocalDateTime.of(2020, 6, 14, 10, 0);
        Long productId = 35455L;
        Long brandId = 1L;

        when(priceRepository.findApplicablePrice(date, productId, brandId)).thenReturn(Mono.error(new RuntimeException("BD fail")));

        StepVerifier.create(priceService.findApplicablePrice(date, productId, brandId))
                .expectError(ResponseStatusException.class)
                .verify();
    }
}
