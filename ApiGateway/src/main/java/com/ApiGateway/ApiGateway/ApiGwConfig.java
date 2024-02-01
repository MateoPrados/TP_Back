package com.ApiGateway.ApiGateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtGrantedAuthoritiesConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;


@Configuration
@EnableWebFluxSecurity
public class ApiGwConfig {
    @Bean
    public RouteLocator configurarRutas(RouteLocatorBuilder builder,
                                        @Value("${url-estaciones}") String uriEstaciones,
                                        @Value("${url-alquileres}") String uriAlquileres) {
        return builder.routes()
                // Ruteo al Microservicio de Personas
                .route(p -> p.path("/microservicio/estaciones/**").uri(uriEstaciones))
                // Ruteo al Microservicio de Entradas
                .route(p -> p.path("/microservicio/alquileres/**").uri(uriAlquileres))
                .build();
    }


    @Bean
    public SecurityWebFilterChain filterChain(ServerHttpSecurity http) throws
            Exception {
        http.authorizeExchange(exchanges -> exchanges
                                // Esta ruta puede ser accedida por cualquiera, sin autorización
                                .pathMatchers(HttpMethod.GET, "/microservicio/estaciones/**")
                                .hasRole("CLIENTE")
                                .pathMatchers(HttpMethod.POST, "/microservicio/alquileres/**")
                                .hasRole("CLIENTE")
                                .pathMatchers(HttpMethod.PUT, "/microservicio/alquileres/**")
                                .hasRole("CLIENTE")
                                .pathMatchers(HttpMethod.POST, "/microservicio/estaciones/**")
                                .hasRole("ADMINISTRADOR")
                                .pathMatchers(HttpMethod.GET, "/microservicio/alquileres/**")
                                .hasRole("ADMINISTRADOR")

                ).oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(Customizer.withDefaults()))
                .csrf(csrf -> csrf.disable());
        return http.build();
    }

    @Bean
    public ReactiveJwtAuthenticationConverter jwtAuthenticationConverter() {
        var jwtAuthenticationConverter = new ReactiveJwtAuthenticationConverter();
        var grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthoritiesClaimName("authorities");
        grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new ReactiveJwtGrantedAuthoritiesConverterAdapter(grantedAuthoritiesConverter));
        return jwtAuthenticationConverter;
    }
}