package tr.kontas.erp.reporting.platform.persistence.saved;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface JpaSavedReportRepository extends JpaRepository<SavedReportJpaEntity, UUID> {
    List<SavedReportJpaEntity> findByCreatedByAndTenantId(String createdBy, UUID tenantId);
    List<SavedReportJpaEntity> findBySharedTrueAndTenantId(UUID tenantId);
}

