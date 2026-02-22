package tr.kontas.erp.reporting.platform.persistence.saved;

import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.reporting.domain.definition.ReportDefinitionId;
import tr.kontas.erp.reporting.domain.saved.SavedReport;
import tr.kontas.erp.reporting.domain.saved.SavedReportId;

public class SavedReportMapper {

    public static SavedReport toDomain(SavedReportJpaEntity e) {
        return new SavedReport(
                SavedReportId.of(e.getId()),
                TenantId.of(e.getTenantId()),
                ReportDefinitionId.of(e.getReportDefinitionId()),
                e.getName(),
                e.getSavedFiltersJson(),
                e.getSavedSortsJson(),
                e.isShared(),
                e.getCreatedBy(),
                e.getCreatedAt()
        );
    }

    public static SavedReportJpaEntity toJpa(SavedReport d) {
        return new SavedReportJpaEntity(
                d.getId().asUUID(),
                d.getTenantId().asUUID(),
                d.getReportDefinitionId().asUUID(),
                d.getName(),
                d.getSavedFiltersJson(),
                d.getSavedSortsJson(),
                d.isShared(),
                d.getCreatedBy(),
                d.getCreatedAt()
        );
    }
}

