package tr.kontas.erp.app.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tr.kontas.erp.core.platform.configuration.JacksonProvider;

@Configuration
public class ObjectMapperConfig {
    @Bean
    public ObjectMapper objectMapper() {
        return JacksonProvider.get();
    }
}
