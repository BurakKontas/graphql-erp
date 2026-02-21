package tr.kontas.erp.hr.platform.persistence.leavepolicy;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface JpaLeavePolicyRepository extends JpaRepository<LeavePolicyJpaEntity, UUID> {
    List<LeavePolicyJpaEntity> findByTenantIdAndCompanyId(UUID tenantId, UUID companyId);
    List<LeavePolicyJpaEntity> findByTenantIdAndId(UUID tenantId, UUID id);
}
