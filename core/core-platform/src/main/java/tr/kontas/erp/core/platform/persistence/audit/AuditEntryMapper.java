package tr.kontas.erp.core.platform.persistence.audit;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import tr.kontas.erp.core.domain.audit.AuditEntry;
import tr.kontas.erp.core.domain.audit.FieldChange;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AuditEntryMapper {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static AuditEntryJpaEntity toEntity(AuditEntry a) {
        AuditEntryJpaEntity e = new AuditEntryJpaEntity();
        e.setId(a.getId());
        e.setSource(a.getSource());
        e.setTenantId(a.getTenantId());
        e.setCompanyId(a.getCompanyId());
        e.setModuleName(a.getModuleName());
        e.setAggregateType(a.getAggregateType());
        e.setAggregateId(a.getAggregateId());
        e.setAction(a.getAction());
        e.setEventType(a.getEventType());
        e.setUserId(a.getUserId());
        e.setUserEmail(a.getUserEmail());
        e.setUserIp(a.getUserIp());
        e.setUserAgent(a.getUserAgent());
        e.setOccurredAt(a.getOccurredAt());
        e.setBeforeSnapshot(a.getBeforeSnapshot());
        e.setAfterSnapshot(a.getAfterSnapshot());
        e.setCorrelationId(a.getCorrelationId());
        e.setSessionId(a.getSessionId());
        try {
            if (a.getChanges() != null && !a.getChanges().isEmpty()) {
                e.setChangesJson(MAPPER.writeValueAsString(a.getChanges().stream().map(fc -> Map.of(
                        "fieldName", fc.getFieldName(),
                        "oldValue", fc.getOldValue() != null ? fc.getOldValue() : "",
                        "newValue", fc.getNewValue() != null ? fc.getNewValue() : "",
                        "maskLevel", fc.getMaskLevel() != null ? fc.getMaskLevel() : "NONE"
                )).toList()));
            }
        } catch (Exception ignored) {
        }
        return e;
    }

    public static AuditEntry toDomain(AuditEntryJpaEntity e) {
        List<FieldChange> changes = new ArrayList<>();
        try {
            if (e.getChangesJson() != null && !e.getChangesJson().isBlank()) {
                List<Map<String, String>> raw = MAPPER.readValue(e.getChangesJson(), new TypeReference<>() {});
                for (Map<String, String> m : raw) {
                    changes.add(new FieldChange(
                            m.get("fieldName"),
                            m.get("oldValue"),
                            m.get("newValue"),
                            m.get("maskLevel")
                    ));
                }
            }
        } catch (Exception ignored) {
        }
        return new AuditEntry(
                e.getId(), e.getSource(), e.getTenantId(), e.getCompanyId(),
                e.getModuleName(), e.getAggregateType(), e.getAggregateId(),
                e.getAction(), e.getEventType(),
                e.getUserId(), e.getUserEmail(), e.getUserIp(), e.getUserAgent(),
                e.getOccurredAt(),
                e.getBeforeSnapshot(), e.getAfterSnapshot(), changes,
                e.getCorrelationId(), e.getSessionId()
        );
    }
}

