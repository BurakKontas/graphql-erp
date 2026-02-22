package tr.kontas.erp.reporting.platform.persistence.definition;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface JpaReportDefinitionRepository extends JpaRepository<ReportDefinitionJpaEntity, UUID> {
    List<ReportDefinitionJpaEntity> findByTenantId(UUID tenantId);
}

