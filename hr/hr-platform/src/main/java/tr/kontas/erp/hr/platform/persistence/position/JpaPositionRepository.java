package tr.kontas.erp.hr.platform.persistence.position;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface JpaPositionRepository extends JpaRepository<PositionJpaEntity, UUID> {
    List<PositionJpaEntity> findByTenantIdAndCompanyId(UUID tenantId, UUID companyId);
    List<PositionJpaEntity> findByTenantIdAndId(UUID tenantId, UUID id);
}
