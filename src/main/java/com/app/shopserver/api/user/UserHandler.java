package com.app.shopserver.api.user;

import com.app.shopserver.util.JwtTokenProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class UserHandler {

    public static final String SECRET = "SECRET_KEY";
    public static final long EXPIRATION_TIME_MINUTES = 10;

    private final UserRepository userRepository;
    private final UserService userService;
    private final JwtTokenProvider tokenProvider;
    private final ReactiveAuthenticationManager authenticationManager;

    public UserHandler(UserRepository userRepository, UserService userService, JwtTokenProvider tokenProvider, ReactiveAuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.tokenProvider = tokenProvider;
        this.authenticationManager = authenticationManager;
    }

    public Mono<ServerResponse> login(ServerRequest request) {
        return request.bodyToMono(UserModel.class)
                .flatMap(userModel -> login(userModel.username(), userModel.password()));
    }

    public Mono<ServerResponse> login(String username, String password) {
        return authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(username, password)
                )
                .map(tokenProvider::createToken)
                .flatMap(jwt -> userRepository.findByUsername(username)
                        .map(userFound -> new UserDto(userFound, jwt)))
                .flatMap(userDto ->
                        ServerResponse.ok()
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + userDto.jwt())
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(BodyInserters.fromValue(userDto))
                );
    }

    public Mono<ServerResponse> register(ServerRequest request) {
        return request.bodyToMono(UserModel.class)
                .map(UserModel::generateCreationCopy)
                .flatMap(userService::register)
                .flatMap(userDto ->
                        ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(BodyInserters.fromValue(userDto))
                );
    }

    public Mono<ServerResponse> getInfo(ServerRequest request) {
        return request.principal()
                .flatMap(principal -> userRepository.findByUsername(principal.getName()))
                .flatMap(user -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromValue(user))
                );
    }

}
