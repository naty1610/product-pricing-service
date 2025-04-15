package com.capitole.product.pricing.application.handler.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Respuesta con el precio aplicable")
public record PriceResponse(
        @Schema(description = "ID del producto", example = "35455")
        Long productId,

        @Schema(description = "ID de la marca", example = "1")
        Long brandId,

        @Schema(description = "ID de la tarifa o price list", example = "1")
        Long priceList,

        @Schema(description = "Fecha de inicio de validez del precio", example = "2020-06-14T00:00:00")
        LocalDateTime startDate,

        @Schema(description = "Fecha de fin de validez del precio", example = "2020-12-31T23:59:59")
        LocalDateTime endDate,

        @Schema(description = "Precio final", example = "35.5")
        BigDecimal price
) {}
