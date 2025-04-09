package com.capitole.product.pricing.application.router;

import com.capitole.product.pricing.application.handler.ProductPricingHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@WebFluxTest
@ContextConfiguration(classes = {ProductPricingRouter.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class ProductPricingRouterTest {

    @MockBean
    private ProductPricingHandler productPricingHandler;

    @InjectMocks
    private ProductPricingRouter productPricingRouter;

    @Autowired private WebTestClient webTestClient;

    @Test
    void testPricingRouterGetPath_Success() {
        when(productPricingHandler.handleRequest(any(ServerRequest.class)))
                .thenReturn(ServerResponse.ok().bodyValue("Mocked response"));

        webTestClient.get()
                .uri(ProductPricingRouter.PATH_PRODUCT_PRICING)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo("Mocked response");

        verify(productPricingHandler, times(1)).handleRequest(any(ServerRequest.class));
    }

    @Test
    void testPricingRouterGetPath_Error() {
        when(productPricingHandler.handleRequest(any(ServerRequest.class)))
                .thenReturn(ServerResponse.ok().bodyValue("Mocked response"));

        webTestClient.get()
                .uri("url/test")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .is4xxClientError();
    }

}
