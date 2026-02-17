package tr.kontas.erp.core.platform.context;

import java.util.Set;
import java.util.UUID;

public record Context(String userId, UUID tenantId, Set<String> permissions) {
}
