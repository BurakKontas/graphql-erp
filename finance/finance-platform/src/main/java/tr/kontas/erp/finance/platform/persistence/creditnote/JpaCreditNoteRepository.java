package tr.kontas.erp.finance.platform.persistence.creditnote;

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
public interface JpaCreditNoteRepository extends JpaRepository<CreditNoteJpaEntity, UUID> {
    Optional<CreditNoteJpaEntity> findByIdAndTenantId(UUID id, UUID tenantId);
    List<CreditNoteJpaEntity> findByTenantIdAndCompanyId(UUID tenantId, UUID companyId);
    List<CreditNoteJpaEntity> findByIdIn(List<UUID> ids);

    @Query("SELECT COALESCE(MAX(CAST(SUBSTRING(e.creditNoteNumber, 9) AS int)), 0) FROM CreditNoteJpaEntity e WHERE e.tenantId = :tenantId")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    int findMaxSequenceByTenantId(@Param("tenantId") UUID tenantId);
}

