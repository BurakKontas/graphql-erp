package tr.kontas.erp.crm.platform.persistence.lead;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaLeadRepository extends JpaRepository<LeadJpaEntity, UUID> {
    Optional<LeadJpaEntity> findByIdAndTenantId(UUID id, UUID tenantId);
    List<LeadJpaEntity> findByTenantIdAndCompanyId(UUID tenantId, UUID companyId);
    List<LeadJpaEntity> findByIdIn(List<UUID> ids);

    @Query("SELECT COALESCE(MAX(CAST(SUBSTRING(l.leadNumber, LENGTH(l.leadNumber) - 5) AS int)), 0) FROM LeadJpaEntity l WHERE l.tenantId = :tenantId")
    int findMaxSequenceByTenantId(UUID tenantId);
}

