package tr.kontas.erp.reporting.domain.saved;

import lombok.AllArgsConstructor;
import lombok.Getter;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.reporting.domain.definition.ReportDefinitionId;

import java.time.Instant;

@Getter
@AllArgsConstructor
public class SavedReport {

    private SavedReportId id;
    private TenantId tenantId;
    private ReportDefinitionId reportDefinitionId;
    private String name;
    private String savedFiltersJson;
    private String savedSortsJson;
    private boolean shared;
    private String createdBy;
    private Instant createdAt;
}

