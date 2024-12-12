import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class corsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Permite el mapeo CORS para todas las rutas.
                .allowedOrigins(
                        "http://localhost:5173",
                        "https://dh-py-fe-trial.vercel.app/")
                .allowedMethods("GET", "POST", "PUT", "DELETE") // Permite solo estos métodos HTTP.
                .allowedHeaders("Authorization",
                        "Accept",
                        "Cache-Control",
                        "Content-Type",
                        "Origin",
                        "x-csrf-token",
                        "x-requested-with")
                .allowCredentials(true); // Permite el envío de credenciales (cookies, encabezados de autenticación, etc.).
    }
}