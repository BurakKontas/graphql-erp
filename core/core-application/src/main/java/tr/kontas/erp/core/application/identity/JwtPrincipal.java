package tr.kontas.erp.core.application.identity;

import java.util.Set;
import java.util.UUID;

public record JwtPrincipal(
        UUID userId,
        UUID tenantId,
        String username,
        Set<String> roles,
        Set<String> permissions,
        long authVersion
) {
}