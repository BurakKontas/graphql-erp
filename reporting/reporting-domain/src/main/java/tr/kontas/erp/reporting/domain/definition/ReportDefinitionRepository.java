package tr.kontas.erp.reporting.domain.definition;

import tr.kontas.erp.core.kernel.multitenancy.TenantId;

import java.util.List;
import java.util.Optional;

public interface ReportDefinitionRepository {
    void save(ReportDefinition definition);
    Optional<ReportDefinition> findById(ReportDefinitionId id, TenantId tenantId);
    List<ReportDefinition> findByTenantId(TenantId tenantId);
    List<ReportDefinition> findByIds(List<ReportDefinitionId> ids);
}

