package ec.gob.salud.hce.backend.config; // Esto corrige el primer error

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull; // Importante para el segundo error
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(@NonNull CorsRegistry registry) {
        registry.addMapping("/**") // Esto cubre /api/pacientes, /api/consultas, TODO.
                .allowedOriginPatterns("*") // Permite cualquier origen con credenciales
                .allowedMethods("*") // Permite GET, POST, PUT, DELETE, etc.
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}