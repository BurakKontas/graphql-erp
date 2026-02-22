package tr.kontas.erp.finance.platform.persistence.journalentry;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaJournalEntryRepository extends JpaRepository<JournalEntryJpaEntity, UUID> {
    Optional<JournalEntryJpaEntity> findByIdAndTenantId(UUID id, UUID tenantId);
    List<JournalEntryJpaEntity> findByTenantIdAndCompanyId(UUID tenantId, UUID companyId);
    List<JournalEntryJpaEntity> findByTenantIdAndReferenceTypeAndReferenceId(UUID tenantId, String referenceType, String referenceId);
    List<JournalEntryJpaEntity> findByIdIn(List<UUID> ids);

    @Query("SELECT COALESCE(MAX(CAST(SUBSTRING(e.entryNumber, 9) AS int)), 0) FROM JournalEntryJpaEntity e WHERE e.tenantId = :tenantId")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    int findMaxSequenceByTenantId(@Param("tenantId") UUID tenantId);
}

