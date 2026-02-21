package tr.kontas.erp.core.platform.context;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class BootstrapApiKeyProvider {

    @Value("${bootstrap.api.key}")
    private String configuredApiKey;

    private final String apiKey;

    public BootstrapApiKeyProvider() {
        this.apiKey = configuredApiKey != null && !configuredApiKey.isBlank() ? configuredApiKey : UUID.randomUUID().toString();
        if(!this.apiKey.equals(configuredApiKey)) {
            System.out.println();
            System.out.println("╔══════════════════════════════════════════════════════════════╗");
            System.out.println("║  BOOTSTRAP API KEY (GENERAL:ADMIN)                           ║");
            System.out.println("║  " + apiKey + "                        ║");
            System.out.println("║  This key changes on every restart.                          ║");
            System.out.println("╚══════════════════════════════════════════════════════════════╝");
            System.out.println();
        }
    }

    public boolean isValid(String key) {
        return apiKey.equals(key);
    }
}
