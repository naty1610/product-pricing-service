package com.capitole.product.pricing.application.router;

import com.capitole.product.pricing.application.handler.ProductPricingHandler;
import com.capitole.product.pricing.application.handler.response.PriceResponse;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Service
@RequiredArgsConstructor
@OpenAPIDefinition(
        info = @Info(title = "Product Pricing API", version = "1.0", description = "Consulta de precios aplicables")
)
/**
 * Router del servicio Product Pricing
 */
public class ProductPricingRouter {
    public static final String PATH_PRODUCT_PRICING = "/v1/capitole/product/pricing/applicable";
    private final ProductPricingHandler productPricingHandler;

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = PATH_PRODUCT_PRICING,
                    beanClass = ProductPricingHandler.class,
                    beanMethod = "handleRequest",
                    operation = @Operation(
                            operationId = "getApplicablePrice",
                            summary = "Obtiene el precio aplicable de un producto",
                            parameters = {
                                    @Parameter(name = "date", in = ParameterIn.QUERY, description = "Fecha en formato ISO 8601"),
                                    @Parameter(name = "product_id", in = ParameterIn.QUERY, description = "ID del producto"),
                                    @Parameter(name = "brand_id", in = ParameterIn.QUERY, description = "ID de la marca")
                            },
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Precio encontrado",
                                            content = @Content(schema = @Schema(implementation = PriceResponse.class))),
                                    @ApiResponse(responseCode = "400", description = "Parámetros inválidos"),
                                    @ApiResponse(responseCode = "404", description = "No se encontró precio aplicable"),
                                    @ApiResponse(responseCode = "500", description = "Error interno del servidor")
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> productPricingRoute(){ return pricingRoute(); }

    private RouterFunction<ServerResponse> pricingRoute(){
        return route()
                .GET(PATH_PRODUCT_PRICING,
                        RequestPredicates.accept(MediaType.APPLICATION_JSON),
                        productPricingHandler::handleRequest)
                .build();
    }
}
