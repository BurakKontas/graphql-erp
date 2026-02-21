package tr.kontas.erp.core.domain.audit;

import lombok.Getter;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
public class AuditEntry {

    private final UUID id;
    private final String source;
    private final UUID tenantId;
    private final String companyId;
    private final String moduleName;
    private final String aggregateType;
    private final String aggregateId;
    private final String action;
    private final String eventType;
    private final String userId;
    private final String userEmail;
    private final String userIp;
    private final String userAgent;
    private final Instant occurredAt;
    private final String beforeSnapshot;
    private final String afterSnapshot;
    private final List<FieldChange> changes;
    private final String correlationId;
    private final String sessionId;

    public AuditEntry(UUID id, String source, UUID tenantId, String companyId,
                      String moduleName, String aggregateType, String aggregateId,
                      String action, String eventType,
                      String userId, String userEmail, String userIp, String userAgent,
                      Instant occurredAt,
                      String beforeSnapshot, String afterSnapshot,
                      List<FieldChange> changes,
                      String correlationId, String sessionId) {
        this.id = id;
        this.source = source;
        this.tenantId = tenantId;
        this.companyId = companyId;
        this.moduleName = moduleName;
        this.aggregateType = aggregateType;
        this.aggregateId = aggregateId;
        this.action = action;
        this.eventType = eventType;
        this.userId = userId;
        this.userEmail = userEmail;
        this.userIp = userIp;
        this.userAgent = userAgent;
        this.occurredAt = occurredAt;
        this.beforeSnapshot = beforeSnapshot;
        this.afterSnapshot = afterSnapshot;
        this.changes = changes != null ? List.copyOf(changes) : List.of();
        this.correlationId = correlationId;
        this.sessionId = sessionId;
    }
}

