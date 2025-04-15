package com.capitole.product.pricing.infrastructure.adapter.repository;

import com.capitole.product.pricing.domain.exception.NotFoundNoSQLServiceException;
import com.capitole.product.pricing.domain.exception.ProductPricingException;
import com.capitole.product.pricing.domain.model.Price;
import com.capitole.product.pricing.domain.repository.PriceRepository;
import com.capitole.product.pricing.infrastructure.adapter.entity.PriceEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import static com.capitole.product.pricing.domain.exception.NotFoundNoSQLServiceException.NOT_FOUND_DATA_CODE_ERROR;

@Repository
public class MongoPriceRepository implements PriceRepository {

    private static final String BRAND_ID = "brandId";
    private static final String END_DATE = "endDate";
    private static final String PRIORITY = "priority";
    private static final String PRODUCT_ID = "productId";
    private static final String START_DATE = "startDate";

    private final ReactiveMongoTemplate mongoTemplate;

    public MongoPriceRepository(ReactiveMongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    /**
     * Obtiene el precio aplicable para un producto y marca en una fecha dada.
     *
     * @param productId ID del producto
     * @param brandId ID de la marca
     * @param date Fecha de consulta
     * @return Mono con el precio encontrado
     */
    public Mono<Price> findApplicablePrice(LocalDateTime date, Long productId, Long brandId) {
        Query query = new Query();
        query.addCriteria(Criteria.where(PRODUCT_ID).is(productId)
                .and(BRAND_ID).is(brandId)
                .and(START_DATE).lte(date)
                .and(END_DATE).gte(date));
        query.with(Sort.by(Sort.Direction.DESC, PRIORITY));
        return mongoTemplate.findOne(query, PriceEntity.class)
                .map(this::toDomain)
                .switchIfEmpty(Mono.error(
                        new NotFoundNoSQLServiceException("No se encontró un precio para la información suministrada",
                                NOT_FOUND_DATA_CODE_ERROR))
                )
                .onErrorMap(e -> new ProductPricingException(e.getMessage(), NOT_FOUND_DATA_CODE_ERROR, e));
    }

    private Price toDomain(PriceEntity entity) {
        return new Price(
                entity.getProductId(),
                entity.getBrandId(),
                entity.getPriceList(),
                entity.getStartDate(),
                entity.getEndDate(),
                entity.getPrice()
        );
    }
}