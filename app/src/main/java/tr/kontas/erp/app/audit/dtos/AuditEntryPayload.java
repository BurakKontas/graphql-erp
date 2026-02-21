package tr.kontas.erp.app.audit.dtos;

import java.util.List;

public record AuditEntryPayload(
        String id,
        String source,
        String tenantId,
        String companyId,
        String moduleName,
        String aggregateType,
        String aggregateId,
        String action,
        String eventType,
        String userId,
        String userEmail,
        String userIp,
        String userAgent,
        String occurredAt,
        String beforeSnapshot,
        String afterSnapshot,
        List<FieldChangePayload> changes,
        String correlationId,
        String sessionId
) {}

