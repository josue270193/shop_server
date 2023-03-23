package com.app.shopserver.config;

import com.app.shopserver.util.JwtTokenAuthenticationFilter;
import com.app.shopserver.util.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    private final ReactiveUserDetailsService reactiveUserDetailsService;
    private final JwtTokenProvider tokenProvider;

    public SecurityConfig(ReactiveUserDetailsService reactiveUserDetailsService, JwtTokenProvider tokenProvider) {
        this.reactiveUserDetailsService = reactiveUserDetailsService;
        this.tokenProvider = tokenProvider;
    }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .csrf().disable()
                .httpBasic().disable()
                .authenticationManager(customAuthenticationManager())
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers(GET, "/product", "/product/**").permitAll()
                        .pathMatchers(PUT, "/user").permitAll()
                        .pathMatchers(POST, "/user").permitAll()
                        .anyExchange().authenticated()
                )
                .addFilterAt(new JwtTokenAuthenticationFilter(tokenProvider), SecurityWebFiltersOrder.HTTP_BASIC)
        ;
        return http.build();
    }

    @Bean
    public ReactiveAuthenticationManager customAuthenticationManager() {
        var authenticationManager = new UserDetailsRepositoryReactiveAuthenticationManager(reactiveUserDetailsService);
        authenticationManager.setPasswordEncoder(passwordEncoder());
        return authenticationManager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
