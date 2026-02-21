package tr.kontas.erp.core.platform.persistence.audit;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import tr.kontas.erp.core.domain.audit.AuditEntry;
import tr.kontas.erp.core.domain.audit.AuditEntryRepository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class AuditEntryRepositoryImpl implements AuditEntryRepository {

    private final JpaAuditEntryRepository jpa;

    @Override
    public void save(AuditEntry entry) {
        jpa.save(AuditEntryMapper.toEntity(entry));
    }

    @Override
    public List<AuditEntry> findByAggregate(String aggregateType, String aggregateId, UUID tenantId) {
        return jpa.findByAggregateTypeAndAggregateIdAndTenantIdOrderByOccurredAtDesc(aggregateType, aggregateId, tenantId)
                .stream().map(AuditEntryMapper::toDomain).toList();
    }

    @Override
    public List<AuditEntry> findByUser(String userId, UUID tenantId, Instant from, Instant to) {
        return jpa.findByUserIdAndTenantIdAndOccurredAtBetweenOrderByOccurredAtDesc(userId, tenantId, from, to)
                .stream().map(AuditEntryMapper::toDomain).toList();
    }

    @Override
    public List<AuditEntry> findByModule(String moduleName, UUID tenantId, Instant from, Instant to) {
        return jpa.findByModuleNameAndTenantIdAndOccurredAtBetweenOrderByOccurredAtDesc(moduleName, tenantId, from, to)
                .stream().map(AuditEntryMapper::toDomain).toList();
    }

    @Override
    public List<AuditEntry> findAll(UUID tenantId, Instant from, Instant to, int limit, int offset) {
        return jpa.findAllByTenantAndRange(tenantId, from, to)
                .stream().skip(offset).limit(limit).map(AuditEntryMapper::toDomain).toList();
    }

    @Override
    public void deleteOlderThan(Instant cutoff, String moduleName) {
        jpa.deleteByOccurredAtBeforeAndModuleName(cutoff, moduleName);
    }
}

