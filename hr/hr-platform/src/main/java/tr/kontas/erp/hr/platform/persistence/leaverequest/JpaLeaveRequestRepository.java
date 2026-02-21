package tr.kontas.erp.hr.platform.persistence.leaverequest;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface JpaLeaveRequestRepository extends JpaRepository<LeaveRequestJpaEntity, UUID> {
    List<LeaveRequestJpaEntity> findByTenantIdAndCompanyId(UUID tenantId, UUID companyId);
    List<LeaveRequestJpaEntity> findByTenantIdAndId(UUID tenantId, UUID id);
}
