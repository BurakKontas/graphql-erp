package tr.kontas.erp.crm.platform.persistence.opportunity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaOpportunityRepository extends JpaRepository<OpportunityJpaEntity, UUID> {
    Optional<OpportunityJpaEntity> findByIdAndTenantId(UUID id, UUID tenantId);
    List<OpportunityJpaEntity> findByTenantIdAndCompanyId(UUID tenantId, UUID companyId);
    List<OpportunityJpaEntity> findByIdIn(List<UUID> ids);

    @Query("SELECT COALESCE(MAX(CAST(SUBSTRING(o.opportunityNumber, LENGTH(o.opportunityNumber) - 5) AS int)), 0) FROM OpportunityJpaEntity o WHERE o.tenantId = :tenantId")
    int findMaxSequenceByTenantId(UUID tenantId);
}

