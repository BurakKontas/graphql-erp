package tr.kontas.erp.core.platform.audit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tr.kontas.erp.core.application.audit.AuditWriter;
import tr.kontas.erp.core.domain.audit.AuditEntry;
import tr.kontas.erp.core.domain.audit.FieldChange;
import tr.kontas.erp.core.kernel.audit.*;
import tr.kontas.erp.core.platform.multitenancy.TenantContext;

import java.lang.reflect.Field;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
public class AuditEntityListener {

    private static AuditWriter auditWriter;
    private static final ObjectMapper MAPPER = new ObjectMapper()
            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

    public static void setAuditWriter(AuditWriter writer) {
        auditWriter = writer;
    }

    @PostPersist
    public void onPostPersist(Object entity) {
        handleAudit(entity, AuditAction.CREATE);
    }

    @PostUpdate
    public void onPostUpdate(Object entity) {
        handleAudit(entity, AuditAction.UPDATE);
    }

    @PostRemove
    public void onPostRemove(Object entity) {
        handleAudit(entity, AuditAction.DELETE);
    }

    private void handleAudit(Object entity, AuditAction action) {
        if (auditWriter == null) {
            return;
        }
        Class<?> clazz = entity.getClass();
        Auditable auditable = clazz.getAnnotation(Auditable.class);
        if (auditable == null) {
            return;
        }
        try {
            String moduleName = auditable.module();
            String aggregateType = clazz.getSimpleName().replace("JpaEntity", "");
            String aggregateId = extractId(entity);
            UUID tenantId = extractTenantId(entity);
            String snapshot = buildMaskedSnapshot(entity);
            List<FieldChange> changes = buildFieldChanges(entity);

            AuditEntry auditEntry = new AuditEntry(
                    UUID.randomUUID(),
                    AuditSource.JPA_INTERCEPTOR.name(),
                    tenantId,
                    extractCompanyId(entity),
                    moduleName, aggregateType, aggregateId,
                    action.name(), null,
                    null, null, null, null,
                    Instant.now(),
                    action == AuditAction.DELETE ? snapshot : null,
                    action != AuditAction.DELETE ? snapshot : null,
                    changes, null, null
            );
            auditWriter.write(auditEntry);
        } catch (Exception e) {
            log.warn("Failed to write audit entry for {}: {}", clazz.getSimpleName(), e.getMessage());
        }
    }

    private String extractId(Object entity) {
        try {
            Field idField = findField(entity.getClass(), "id");
            if (idField != null) {
                idField.setAccessible(true);
                Object val = idField.get(entity);
                return val != null ? val.toString() : "unknown";
            }
        } catch (Exception ignored) {
        }
        return "unknown";
    }

    private UUID extractTenantId(Object entity) {
        try {
            Field field = findField(entity.getClass(), "tenantId");
            if (field != null) {
                field.setAccessible(true);
                Object val = field.get(entity);
                if (val instanceof UUID uuid) {
                    return uuid;
                }
            }
        } catch (Exception ignored) {
        }
        try {
            return TenantContext.get().asUUID();
        } catch (Exception ignored) {
        }
        return UUID.fromString("00000000-0000-0000-0000-000000000000");
    }

    private String extractCompanyId(Object entity) {
        try {
            Field field = findField(entity.getClass(), "companyId");
            if (field != null) {
                field.setAccessible(true);
                Object val = field.get(entity);
                return val != null ? val.toString() : null;
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    private String buildMaskedSnapshot(Object entity) {
        try {
            java.util.Map<String, Object> map = new java.util.LinkedHashMap<>();
            for (Field field : entity.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                Object val = field.get(entity);
                AuditMask mask = field.getAnnotation(AuditMask.class);
                if (mask != null && val instanceof String strVal) {
                    map.put(field.getName(), MaskingEngine.mask(strVal, mask.level()));
                } else {
                    map.put(field.getName(), val != null ? val.toString() : null);
                }
            }
            return MAPPER.writeValueAsString(map);
        } catch (Exception e) {
            return "{}";
        }
    }

    private List<FieldChange> buildFieldChanges(Object entity) {
        List<FieldChange> changes = new ArrayList<>();
        for (Field field : entity.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            AuditMask mask = field.getAnnotation(AuditMask.class);
            String maskLevel = mask != null ? mask.level().name() : MaskLevel.NONE.name();
            changes.add(new FieldChange(field.getName(), null, null, maskLevel));
        }
        return changes;
    }

    private Field findField(Class<?> clazz, String name) {
        while (clazz != null) {
            try {
                return clazz.getDeclaredField(name);
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            }
        }
        return null;
    }
}

