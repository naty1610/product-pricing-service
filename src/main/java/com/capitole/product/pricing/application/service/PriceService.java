package com.capitole.product.pricing.application.service;

import com.capitole.product.pricing.domain.repository.PriceRepository;
import com.capitole.product.pricing.application.handler.response.PriceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PriceService {

    private final PriceRepository priceRepository;

    public Mono<PriceResponse> findApplicablePrice(LocalDateTime date, Long productId, Long brandId) {
        return priceRepository.findApplicablePrice(date, productId, brandId)
                .map(price -> new PriceResponse(
                        price.productId(),
                        price.brandId(),
                        price.priceList(),
                        price.startDate(),
                        price.endDate(),
                        price.price()
                ))
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Precio no encontrado")))
                .onErrorMap(error -> {
                    if (ResponseStatusException.class.getName().equals(error.getClass().getName())) {
                        return error;
                    }
                    return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, error.getMessage(), error);
                });
    }
}
