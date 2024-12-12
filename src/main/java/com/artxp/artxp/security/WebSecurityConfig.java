package com.artxp.artxp.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    /*
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(authorizeRequests ->
                        authorizeRequests.anyRequest().permitAll()
                )
                .csrf(csrf -> csrf.disable()); // Disable CSRF (Cross-Site Request Forgery)

        return http.build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Deshabilitar protección CSRF
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable())) // Habilitar consola H2
                .authorizeHttpRequests(authz -> authz
                        .anyRequest().permitAll() // Permitir acceso sin autenticación a todas las peticiones
                );
        return http.build();
    }*/
}
