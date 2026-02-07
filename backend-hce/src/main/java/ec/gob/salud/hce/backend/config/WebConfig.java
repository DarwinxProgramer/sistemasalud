package ec.gob.salud.hce.backend.config; // Esto corrige el primer error

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull; // Importante para el segundo error
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(@NonNull CorsRegistry registry) { // @NonNull corrige la advertencia
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:5173", "http://localhost")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}