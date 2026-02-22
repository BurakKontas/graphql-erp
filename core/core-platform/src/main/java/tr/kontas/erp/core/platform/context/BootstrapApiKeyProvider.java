package tr.kontas.erp.core.platform.context;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class BootstrapApiKeyProvider {

    @Value("${bootstrap.api.key:}")
    private String configuredApiKey;

    private String apiKey;

    @PostConstruct
    void init() {
        if (configuredApiKey != null && !configuredApiKey.isBlank()) {
            this.apiKey = configuredApiKey;
            log.info("Bootstrap API key loaded from configuration");
        } else {
            this.apiKey = UUID.randomUUID().toString();
            log.warn("""
                    
                    ╔══════════════════════════════════════════════════════════════╗
                    ║  BOOTSTRAP API KEY (GENERAL:ADMIN)                           ║
                    ║  {}                        ║
                    ║  This key changes on every restart.                          ║
                    ╚══════════════════════════════════════════════════════════════╝
                    """, apiKey);
        }
    }

    public boolean isValid(String key) {
        return apiKey.equals(key);
    }
}
