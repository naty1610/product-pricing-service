package com.capitole.product.pricing;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class ProductPricingApplication {
	public static void main(String[] args) {
		new SpringApplicationBuilder(ProductPricingApplication.class).registerShutdownHook(true).run(args);
	}
}
