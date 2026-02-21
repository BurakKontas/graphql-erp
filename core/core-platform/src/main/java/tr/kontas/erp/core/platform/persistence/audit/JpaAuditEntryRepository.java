package tr.kontas.erp.core.platform.persistence.audit;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
public interface JpaAuditEntryRepository extends JpaRepository<AuditEntryJpaEntity, UUID> {
    List<AuditEntryJpaEntity> findByAggregateTypeAndAggregateIdAndTenantIdOrderByOccurredAtDesc(String aggregateType, String aggregateId, UUID tenantId);
    List<AuditEntryJpaEntity> findByUserIdAndTenantIdAndOccurredAtBetweenOrderByOccurredAtDesc(String userId, UUID tenantId, Instant from, Instant to);
    List<AuditEntryJpaEntity> findByModuleNameAndTenantIdAndOccurredAtBetweenOrderByOccurredAtDesc(String moduleName, UUID tenantId, Instant from, Instant to);

    @Query("SELECT e FROM AuditEntryJpaEntity e WHERE e.tenantId = :tenantId AND e.occurredAt BETWEEN :from AND :to ORDER BY e.occurredAt DESC")
    List<AuditEntryJpaEntity> findAllByTenantAndRange(UUID tenantId, Instant from, Instant to);

    @Modifying
    @Query("DELETE FROM AuditEntryJpaEntity e WHERE e.occurredAt < :cutoff AND e.moduleName = :moduleName")
    void deleteByOccurredAtBeforeAndModuleName(Instant cutoff, String moduleName);
}

