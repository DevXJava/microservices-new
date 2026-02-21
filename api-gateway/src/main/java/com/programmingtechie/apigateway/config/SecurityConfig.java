package com.programmingtechie.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityWebFilterChain(ServerHttpSecurity serverHttpSecurity){
        serverHttpSecurity.csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchange->exchange

                        //Eureka
                        .pathMatchers("/eureka/**")
                        .permitAll()
                        // Swagger via gateway
                        .pathMatchers(
                                "/product-service/v3/api-docs/**",
                                "/product-service/swagger-ui/**",
                                "/order-service/v3/api-docs/**",
                                "/order-service/swagger-ui/**"
                        ).permitAll()
                        // Allow swagger static resources
                        .pathMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/webjars/**"
                        ).permitAll()
                        .anyExchange()
                        .authenticated())
                .oauth2ResourceServer(outh2->outh2.jwt(Customizer.withDefaults()));
        return serverHttpSecurity.build();


    }
}
