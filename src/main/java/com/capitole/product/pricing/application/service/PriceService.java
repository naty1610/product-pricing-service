package com.capitole.product.pricing.application.service;

import com.capitole.product.pricing.domain.exception.ProductPricingException;
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
/**
 * Servicio que aplica las reglas de negocio para determinar el precio válido.
 */
public class PriceService {

    private final PriceRepository priceRepository;

    /**
     * Obtiene el precio aplicable para un producto y marca en una fecha dada.
     *
     * @param productId ID del producto
     * @param brandId ID de la marca
     * @param date Fecha de consulta
     * @return Mono con el precio encontrado
     */
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
                .onErrorMap(error -> {
                    if (ProductPricingException.class.getName().equals(error.getClass().getName())) {
                        return new ResponseStatusException(HttpStatus.NOT_FOUND, error.getMessage(), error);
                    }
                    return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, error.getMessage(), error);
                });
    }
}
