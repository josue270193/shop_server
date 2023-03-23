package com.app.shopserver.api.product;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface ProductRepository extends ReactiveMongoRepository<ProductModel, String> {

    Mono<ProductModel> findByName(String name);

}
