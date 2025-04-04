package com.capitole.product.pricing.infrastructure.adapter.repository;

import com.capitole.product.pricing.infrastructure.adapter.entity.PriceEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MongoPriceRepositoryTest {
    @Mock private ReactiveMongoTemplate mongoTemplate;
    @InjectMocks private MongoPriceRepository mongoPriceRepository;

    @BeforeEach
    void setUp() {
        mongoTemplate = mock(ReactiveMongoTemplate.class);
        mongoPriceRepository = new MongoPriceRepository(mongoTemplate);
    }

    @Test
    public void testFindApplicablePrice_Success(){
        LocalDateTime date = LocalDateTime.of(2020, 6, 14, 10, 0);
        Long productId = 35455L;
        Long brandId = 1L;
        PriceEntity priceEntity = new PriceEntity();
        priceEntity.setProductId(productId);
        priceEntity.setBrandId(brandId);
        priceEntity.setPriceList(2L);
        priceEntity.setStartDate(LocalDateTime.of(2020, 6, 14, 10, 0));
        priceEntity.setEndDate(LocalDateTime.of(2020, 6, 14, 13, 30));
        priceEntity.setPrice(BigDecimal.valueOf(25.45));
        priceEntity.setCurrency("EUR");

        when(mongoTemplate.findOne(any(Query.class), eq(PriceEntity.class)))
                .thenReturn(Mono.just(priceEntity));

        StepVerifier.create(
                mongoPriceRepository.findApplicablePrice(date, productId, brandId))
                .assertNext(
                        price -> {
                            assertNotNull(price);
                            assertNotNull(price.price());
                            assertNotNull(price.productId());
                            assertNotNull(price.brandId());
                            assertNotNull(price.startDate());
                            assertNotNull(price.endDate());
                            assertNotNull(price.priceList());
                            assertEquals(BigDecimal.valueOf(25.45), price.price());
                            assertEquals(productId, price.productId());
                            assertEquals(brandId, price.brandId());
                            assertEquals(LocalDateTime.of(2020, 6, 14, 10, 0), price.startDate());
                            assertEquals(LocalDateTime.of(2020, 6, 14, 13, 30), price.endDate());
                            assertEquals(2L, price.priceList());
                        }
                ).verifyComplete();
    }
}
