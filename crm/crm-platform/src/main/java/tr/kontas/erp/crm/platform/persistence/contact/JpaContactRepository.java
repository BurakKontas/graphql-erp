package tr.kontas.erp.crm.platform.persistence.contact;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaContactRepository extends JpaRepository<ContactJpaEntity, UUID> {
    Optional<ContactJpaEntity> findByIdAndTenantId(UUID id, UUID tenantId);
    List<ContactJpaEntity> findByTenantIdAndCompanyId(UUID tenantId, UUID companyId);
    List<ContactJpaEntity> findByIdIn(List<UUID> ids);

    @Query("SELECT COALESCE(MAX(CAST(SUBSTRING(c.contactNumber, LENGTH(c.contactNumber) - 5) AS int)), 0) FROM ContactJpaEntity c WHERE c.tenantId = :tenantId")
    int findMaxSequenceByTenantId(UUID tenantId);
}

