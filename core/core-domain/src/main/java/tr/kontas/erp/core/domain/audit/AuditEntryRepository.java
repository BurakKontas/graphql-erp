package tr.kontas.erp.core.domain.audit;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface AuditEntryRepository {
    void save(AuditEntry entry);
    List<AuditEntry> findByAggregate(String aggregateType, String aggregateId, UUID tenantId);
    List<AuditEntry> findByUser(String userId, UUID tenantId, Instant from, Instant to);
    List<AuditEntry> findByModule(String moduleName, UUID tenantId, Instant from, Instant to);
    List<AuditEntry> findAll(UUID tenantId, Instant from, Instant to, int limit, int offset);
    void deleteOlderThan(Instant cutoff, String moduleName);
}

