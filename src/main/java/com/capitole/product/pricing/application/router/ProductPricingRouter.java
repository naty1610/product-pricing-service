package com.capitole.product.pricing.application.router;

import com.capitole.product.pricing.application.handler.ProductPricingHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Service
@RequiredArgsConstructor
public class ProductPricingRouter {
    public static final String PATH_PRODUCT_PRICING = "/v1/capitole/product/pricing/applicable";
    private final ProductPricingHandler productPricingHandler;

    @Bean
    public RouterFunction<ServerResponse> productPricingRoute(){ return pricingRoute(); }

    private RouterFunction<ServerResponse> pricingRoute(){
        return route()
                .GET(PATH_PRODUCT_PRICING,
                        RequestPredicates.accept(MediaType.APPLICATION_JSON),
                        productPricingHandler::handleRequest)
                .build();
    }
}
