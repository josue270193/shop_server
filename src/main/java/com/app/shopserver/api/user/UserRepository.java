package com.app.shopserver.api.user;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveMongoRepository<UserModel, String> {

    Mono<UserModel> findByUsername(String username);

}
