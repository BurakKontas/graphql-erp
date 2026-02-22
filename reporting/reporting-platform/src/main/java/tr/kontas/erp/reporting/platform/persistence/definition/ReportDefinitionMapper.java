package tr.kontas.erp.reporting.platform.persistence.definition;

import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.reporting.domain.ReportModule;
import tr.kontas.erp.reporting.domain.ReportType;
import tr.kontas.erp.reporting.domain.definition.ReportDefinition;
import tr.kontas.erp.reporting.domain.definition.ReportDefinitionId;

public class ReportDefinitionMapper {

    public static ReportDefinition toDomain(ReportDefinitionJpaEntity e) {
        return new ReportDefinition(
                ReportDefinitionId.of(e.getId()),
                TenantId.of(e.getTenantId()),
                e.getName(),
                e.getDescription(),
                ReportModule.valueOf(e.getModule()),
                ReportType.valueOf(e.getType()),
                e.getDataSource(),
                e.getColumnsJson(),
                e.getFiltersJson(),
                e.getSqlQuery(),
                e.getRequiredPermission(),
                e.isSystemReport(),
                e.isActive(),
                e.getCreatedBy()
        );
    }

    public static ReportDefinitionJpaEntity toJpa(ReportDefinition d) {
        return new ReportDefinitionJpaEntity(
                d.getId().asUUID(),
                d.getTenantId().asUUID(),
                d.getName(),
                d.getDescription(),
                d.getModule().name(),
                d.getType().name(),
                d.getDataSource(),
                d.getColumnsJson(),
                d.getFiltersJson(),
                d.getSqlQuery(),
                d.getRequiredPermission(),
                d.isSystemReport(),
                d.isActive(),
                d.getCreatedBy()
        );
    }
}

