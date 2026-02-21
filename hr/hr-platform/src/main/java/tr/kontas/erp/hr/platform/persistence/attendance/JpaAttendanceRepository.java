package tr.kontas.erp.hr.platform.persistence.attendance;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface JpaAttendanceRepository extends JpaRepository<AttendanceJpaEntity, UUID> {
    List<AttendanceJpaEntity> findByTenantIdAndCompanyId(UUID tenantId, UUID companyId);
    List<AttendanceJpaEntity> findByTenantIdAndId(UUID tenantId, UUID id);
}
