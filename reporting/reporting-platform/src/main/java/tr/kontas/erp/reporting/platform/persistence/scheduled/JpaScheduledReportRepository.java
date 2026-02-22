package tr.kontas.erp.reporting.platform.persistence.scheduled;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface JpaScheduledReportRepository extends JpaRepository<ScheduledReportJpaEntity, UUID> {
    List<ScheduledReportJpaEntity> findByTenantId(UUID tenantId);
    List<ScheduledReportJpaEntity> findByActiveTrue();
}

