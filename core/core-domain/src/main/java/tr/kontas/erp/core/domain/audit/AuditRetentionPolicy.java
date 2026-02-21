package tr.kontas.erp.core.domain.audit;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class AuditRetentionPolicy {

    private final UUID id;
    private final UUID tenantId;
    private final String moduleName;
    private final int retentionDays;
}

