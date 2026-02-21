package tr.kontas.erp.app.audit.graphql;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import lombok.RequiredArgsConstructor;
import tr.kontas.erp.app.audit.dtos.AuditEntryPayload;
import tr.kontas.erp.app.audit.dtos.FieldChangePayload;
import tr.kontas.erp.core.application.audit.AuditQueryService;
import tr.kontas.erp.core.domain.audit.AuditEntry;
import tr.kontas.erp.core.platform.multitenancy.TenantContext;

import java.time.Instant;
import java.util.List;

@DgsComponent
@RequiredArgsConstructor
public class AuditGraphql {

    private final AuditQueryService auditQueryService;

    @DgsQuery
    public List<AuditEntryPayload> auditByAggregate(
            @InputArgument String aggregateType,
            @InputArgument String aggregateId) {
        return auditQueryService.findByAggregate(aggregateType, aggregateId, TenantContext.get().asUUID())
                .stream().map(AuditGraphql::toPayload).toList();
    }

    @DgsQuery
    public List<AuditEntryPayload> auditByUser(
            @InputArgument String userId,
            @InputArgument String from,
            @InputArgument String to) {
        return auditQueryService.findByUser(userId, TenantContext.get().asUUID(),
                        Instant.parse(from), Instant.parse(to))
                .stream().map(AuditGraphql::toPayload).toList();
    }

    @DgsQuery
    public List<AuditEntryPayload> auditByModule(
            @InputArgument String moduleName,
            @InputArgument String from,
            @InputArgument String to) {
        return auditQueryService.findByModule(moduleName, TenantContext.get().asUUID(),
                        Instant.parse(from), Instant.parse(to))
                .stream().map(AuditGraphql::toPayload).toList();
    }

    @DgsQuery
    public List<AuditEntryPayload> auditEntries(
            @InputArgument String from,
            @InputArgument String to,
            @InputArgument Integer limit,
            @InputArgument Integer offset) {
        return auditQueryService.findAll(TenantContext.get().asUUID(),
                        Instant.parse(from), Instant.parse(to),
                        limit != null ? limit : 50,
                        offset != null ? offset : 0)
                .stream().map(AuditGraphql::toPayload).toList();
    }

    public static AuditEntryPayload toPayload(AuditEntry a) {
        List<FieldChangePayload> changes = a.getChanges().stream()
                .map(fc -> new FieldChangePayload(
                        fc.getFieldName(), fc.getOldValue(),
                        fc.getNewValue(), fc.getMaskLevel()
                )).toList();
        return new AuditEntryPayload(
                a.getId().toString(), a.getSource(),
                a.getTenantId().toString(), a.getCompanyId(),
                a.getModuleName(), a.getAggregateType(), a.getAggregateId(),
                a.getAction(), a.getEventType(),
                a.getUserId(), a.getUserEmail(), a.getUserIp(), a.getUserAgent(),
                a.getOccurredAt().toString(),
                a.getBeforeSnapshot(), a.getAfterSnapshot(),
                changes, a.getCorrelationId(), a.getSessionId()
        );
    }
}

