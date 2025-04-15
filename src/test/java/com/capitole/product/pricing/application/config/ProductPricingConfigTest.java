package com.capitole.product.pricing.application.config;

import com.capitole.product.pricing.application.service.PriceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class ProductPricingConfigTest {
    private ProductPricingConfig productPricingConfig;
    @Mock private PriceService priceService;

    @BeforeEach
    public void setUp(){
        productPricingConfig = new ProductPricingConfig();
    }

    @Test
    public void testPriceController(){
        assertNotNull(productPricingConfig.priceController(priceService));
    }
}
