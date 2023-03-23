package com.app.shopserver.api.user;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class UserRoute {
    @Bean
    public RouterFunction<ServerResponse> userRoutes(UserHandler userHandler) {
        return RouterFunctions
                .route(PUT("/user").and(accept(MediaType.APPLICATION_JSON)), userHandler::register)
                .andRoute(POST("/user").and(accept(MediaType.APPLICATION_JSON)), userHandler::login)
                .andRoute(GET("/user/info").and(accept(MediaType.APPLICATION_JSON)), userHandler::getInfo)
                ;
    }
}
