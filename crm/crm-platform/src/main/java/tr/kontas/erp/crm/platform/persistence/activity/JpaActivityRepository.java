package tr.kontas.erp.crm.platform.persistence.activity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaActivityRepository extends JpaRepository<ActivityJpaEntity, UUID> {
    Optional<ActivityJpaEntity> findByIdAndTenantId(UUID id, UUID tenantId);
    List<ActivityJpaEntity> findByTenantIdAndCompanyId(UUID tenantId, UUID companyId);
    List<ActivityJpaEntity> findByIdIn(List<UUID> ids);
}

