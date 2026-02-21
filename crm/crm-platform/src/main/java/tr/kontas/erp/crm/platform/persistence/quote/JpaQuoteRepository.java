package tr.kontas.erp.crm.platform.persistence.quote;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaQuoteRepository extends JpaRepository<QuoteJpaEntity, UUID> {
    Optional<QuoteJpaEntity> findByIdAndTenantId(UUID id, UUID tenantId);
    List<QuoteJpaEntity> findByTenantIdAndCompanyId(UUID tenantId, UUID companyId);
    List<QuoteJpaEntity> findByIdIn(List<UUID> ids);

    @Query("SELECT COALESCE(MAX(CAST(SUBSTRING(q.quoteNumber, LENGTH(q.quoteNumber) - 5) AS int)), 0) FROM QuoteJpaEntity q WHERE q.tenantId = :tenantId")
    int findMaxSequenceByTenantId(UUID tenantId);
}

