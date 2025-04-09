package com.capitole.product.pricing.application.config;

import com.capitole.product.pricing.application.service.PriceService;
import com.capitole.product.pricing.infrastructure.adapter.controller.PriceController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProductPricingConfig {

    @Bean
    public PriceController priceController(PriceService priceService){
        return new PriceController(priceService);
    }
}
