package com.capitole.product.pricing.application.router;

import com.capitole.product.pricing.infrastructure.adapter.entity.PriceEntity;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import static com.capitole.product.pricing.application.router.ProductPricingRouter.PATH_PRODUCT_PRICING;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureWebTestClient
@Testcontainers
public class ProductPricingRouterIntegrationTest {

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:6.0");

    @DynamicPropertySource
    static void mongoProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ReactiveMongoTemplate mongoTemplate;

    @BeforeEach
    void setUp() {

        List<PriceEntity> prices = getPriceEntityList();

        mongoTemplate.dropCollection(PriceEntity.class)
                .thenMany(mongoTemplate.insertAll(prices))
                .blockLast();
    }

    @Test
    void testProductPricing_ReturnPrice_Case_10_140620() {
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(PATH_PRODUCT_PRICING)
                        .queryParam("date", "2020-06-14T10:00:00")
                        .queryParam("product_id", 35455)
                        .queryParam("brand_id", 1)
                        .build())
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.productId").isEqualTo(35455)
                .jsonPath("$.brandId").isEqualTo(1)
                .jsonPath("$.priceList").isEqualTo(1)
                .jsonPath("$.price").isEqualTo(35.50)
                .jsonPath("$.startDate").isEqualTo("2020-06-14T00:00:00")
                .jsonPath("$.endDate").isEqualTo("2020-12-31T23:59:59");
    }

    @Test
    void testProductPricing_ReturnPrice_Case_16_140620() {
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(PATH_PRODUCT_PRICING)
                        .queryParam("date", "2020-06-14T16:00:00")
                        .queryParam("product_id", 35455)
                        .queryParam("brand_id", 1)
                        .build())
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.productId").isEqualTo(35455)
                .jsonPath("$.brandId").isEqualTo(1)
                .jsonPath("$.priceList").isEqualTo(2)
                .jsonPath("$.price").isEqualTo(25.45)
                .jsonPath("$.startDate").isEqualTo("2020-06-14T15:00:00")
                .jsonPath("$.endDate").isEqualTo("2020-06-14T18:30:00");
    }

    @Test
    void testProductPricing_ReturnPrice_Case_21_140620() {
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(PATH_PRODUCT_PRICING)
                        .queryParam("date", "2020-06-14T21:00:00")
                        .queryParam("product_id", 35455)
                        .queryParam("brand_id", 1)
                        .build())
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.productId").isEqualTo(35455)
                .jsonPath("$.brandId").isEqualTo(1)
                .jsonPath("$.priceList").isEqualTo(1)
                .jsonPath("$.price").isEqualTo(35.50)
                .jsonPath("$.startDate").isEqualTo("2020-06-14T00:00:00")
                .jsonPath("$.endDate").isEqualTo("2020-12-31T23:59:59");
    }

    @Test
    void testProductPricing_ReturnPrice_Case_10_150620() {
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(PATH_PRODUCT_PRICING)
                        .queryParam("date", "2020-06-15T10:00:00")
                        .queryParam("product_id", 35455)
                        .queryParam("brand_id", 1)
                        .build())
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.productId").isEqualTo(35455)
                .jsonPath("$.brandId").isEqualTo(1)
                .jsonPath("$.priceList").isEqualTo(3)
                .jsonPath("$.price").isEqualTo(30.50)
                .jsonPath("$.startDate").isEqualTo("2020-06-15T00:00:00")
                .jsonPath("$.endDate").isEqualTo("2020-06-15T11:00:00");
    }

    @Test
    void testProductPricing_ReturnPrice_Case_21_160620() {
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(PATH_PRODUCT_PRICING)
                        .queryParam("date", "2020-06-16T21:00:00")
                        .queryParam("product_id", 35455)
                        .queryParam("brand_id", 1)
                        .build())
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.productId").isEqualTo(35455)
                .jsonPath("$.brandId").isEqualTo(1)
                .jsonPath("$.priceList").isEqualTo(4)
                .jsonPath("$.price").isEqualTo(38.95)
                .jsonPath("$.startDate").isEqualTo("2020-06-15T16:00:00")
                .jsonPath("$.endDate").isEqualTo("2020-12-31T23:59:59");
    }

    @Test
    void testProductPricing_ReturnNotFound() {
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(PATH_PRODUCT_PRICING)
                        .queryParam("date", "2021-06-16T21:00:00")
                        .queryParam("product_id", 35455)
                        .queryParam("brand_id", 1)
                        .build())
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().is4xxClientError();
    }

    private static @NotNull List<PriceEntity> getPriceEntityList() {
        return List.of(
                getPriceEntity(
                        LocalDateTime.parse("2020-06-14T00:00:00"),
                        LocalDateTime.parse("2020-12-31T23:59:59"),
                        1L,
                        0L,
                        new BigDecimal("35.50")
                ),
                getPriceEntity(
                        LocalDateTime.parse("2020-06-14T15:00:00"),
                        LocalDateTime.parse("2020-06-14T18:30:00"),
                        2L,
                        1L,
                        new BigDecimal("25.45")
                ),
                getPriceEntity(
                        LocalDateTime.parse("2020-06-15T00:00:00"),
                        LocalDateTime.parse("2020-06-15T11:00:00"),
                        3L,
                        1L,
                        new BigDecimal("30.50")
                ),
                getPriceEntity(
                        LocalDateTime.parse("2020-06-15T16:00:00"),
                        LocalDateTime.parse("2020-12-31T23:59:59"),
                        4L,
                        1L,
                        new BigDecimal("38.95")
                )
        );
    }

    private static @NotNull PriceEntity getPriceEntity(LocalDateTime startDate, LocalDateTime endDate,
                                                       Long priceList, Long priority,
                                                       BigDecimal priceProduct) {
        PriceEntity price = new PriceEntity();
        price.setBrandId(1L);
        price.setStartDate(startDate);
        price.setEndDate(endDate);
        price.setPriceList(priceList);
        price.setProductId(35455L);
        price.setPriority(priority);
        price.setPrice(priceProduct);
        price.setCurrency("EUR");
        return price;
    }
}
