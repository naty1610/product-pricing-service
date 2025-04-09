package com.capitole.product.pricing.application.handler;

import com.capitole.product.pricing.domain.exception.DataValidationException;
import com.capitole.product.pricing.infrastructure.adapter.controller.PriceController;
import com.capitole.product.pricing.application.handler.request.PriceParamsRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple3;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import static com.capitole.product.pricing.domain.exception.DataValidationException.*;

@Service
@RequiredArgsConstructor
public class ProductPricingHandler {
    private static final String DATE = "date";
    private static final String BRAND_ID = "brand_id";
    private static final String PRODUCT_ID = "product_id";
    private final PriceController priceController;

    /**
     * Metodo que valida los parametros del request
     */
    public Mono<ServerResponse> handleRequest(ServerRequest serverRequest) {
        return Mono.zip(
                        getRequiredQueryParam(serverRequest, BRAND_ID),
                        getRequiredQueryParam(serverRequest, DATE),
                        getRequiredQueryParam(serverRequest, PRODUCT_ID))
                .flatMap(this::processRequest)
                .onErrorResume(this::handleError);
    }

    private Mono<ServerResponse> processRequest(Tuple3<String, String, String> params) {
        return validateRequest(params)
                .flatMap(priceController::getApplicablePrice)
                .flatMap(priceResponse -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(priceResponse))
                .onErrorResume(this::handleError);
    }

    private Mono<String> getRequiredQueryParam(ServerRequest serverRequest, String paramName) {
        return Mono.justOrEmpty(serverRequest.queryParam(paramName))
                .switchIfEmpty(Mono.error(new DataValidationException(paramName, MESSAGE_ERROR_PARAM_REQUIRED,
                        INVALID_PARAM_CODE_ERROR)));
    }

    private Mono<PriceParamsRequest> validateRequest(Tuple3<String, String, String> params) {
        return Mono.zip(
                parseLong(params.getT1(), BRAND_ID),
                parseDate(params.getT2()),
                parseLong(params.getT3(), PRODUCT_ID)
        ).map(tuple -> new PriceParamsRequest(tuple.getT1(), tuple.getT2(), tuple.getT3()));
    }

   private Mono<Long> parseLong(String value, String paramName) {
        return Mono.fromCallable(() -> Long.parseLong(value))
                .onErrorMap(NumberFormatException.class, e ->
                        new DataValidationException(paramName, MESSAGE_ERROR_INVALID_PARAM, INVALID_PARAM_CODE_ERROR));
    }

    private Mono<LocalDateTime> parseDate(String dateParam) {
        return Mono.fromCallable(() -> LocalDateTime.parse(dateParam, DateTimeFormatter.ISO_DATE_TIME))
                .onErrorMap(DateTimeParseException.class, e ->
                        new DataValidationException(DATE, MESSAGE_ERROR_INVALID_PARAM, INVALID_PARAM_CODE_ERROR));
    }

    private Mono<ServerResponse> handleError(Throwable throwable) {
        if (throwable instanceof DataValidationException) {
            return ServerResponse.badRequest().bodyValue(throwable.getMessage());
        }
        return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).bodyValue("Internal Server Error");
    }

}
