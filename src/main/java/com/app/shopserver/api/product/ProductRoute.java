package com.app.shopserver.api.product;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class ProductRoute {
    @Bean
    public RouterFunction<ServerResponse> productRoutes(ProductHandler productHandler) {
        return RouterFunctions
                .route(GET("/product").and(accept(MediaType.APPLICATION_JSON)), productHandler::getByNameOrAll)
                .andRoute(GET("/product/").and(accept(MediaType.APPLICATION_JSON)), productHandler::getByNameOrAll)
                .andRoute(PUT("/product/random").and(accept(MediaType.APPLICATION_JSON)), productHandler::insertOneRandom)
                .andRoute(GET("/product/{id}").and(accept(MediaType.APPLICATION_JSON)), productHandler::getById)
                ;
    }
}
