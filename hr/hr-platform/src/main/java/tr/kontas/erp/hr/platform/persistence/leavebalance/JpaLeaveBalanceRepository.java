package tr.kontas.erp.hr.platform.persistence.leavebalance;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface JpaLeaveBalanceRepository extends JpaRepository<LeaveBalanceJpaEntity, UUID> {
    List<LeaveBalanceJpaEntity> findByTenantIdAndCompanyId(UUID tenantId, UUID companyId);
    List<LeaveBalanceJpaEntity> findByTenantIdAndId(UUID tenantId, UUID id);
}
