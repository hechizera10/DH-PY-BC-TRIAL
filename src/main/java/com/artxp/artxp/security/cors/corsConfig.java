package com.artxp.artxp.security.cors;//package com.artxp.artxp.security.cors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.stream.Stream;

@Configuration
public class corsConfig implements WebMvcConfigurer {

    @Value("${cors.allowed.origins}")
    private String[] allowedOrigins;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Combina el localhost con las URLs configuradas dinámicamente
        String[] combinedOrigins = Stream.concat(
                Stream.of("http://localhost:5173"), // Siempre incluye localhost
                Stream.of(allowedOrigins)          // Añade las URLs dinámicas
        ).toArray(String[]::new);

        registry.addMapping("/**")
                .allowedOrigins(combinedOrigins) // Usa el array combinado
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("Authorization",
                        "Accept",
                        "Cache-Control",
                        "Content-Type",
                        "Origin",
                        "x-csrf-token",
                        "x-requested-with")
                .allowCredentials(true);
    }
}