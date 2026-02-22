package tr.kontas.erp.reporting.domain.definition;

import lombok.AllArgsConstructor;
import lombok.Getter;
import tr.kontas.erp.core.kernel.multitenancy.TenantId;
import tr.kontas.erp.reporting.domain.ReportModule;
import tr.kontas.erp.reporting.domain.ReportType;

@Getter
@AllArgsConstructor
public class ReportDefinition {

    private ReportDefinitionId id;
    private TenantId tenantId;
    private String name;
    private String description;
    private ReportModule module;
    private ReportType type;
    private String dataSource;
    private String columnsJson;
    private String filtersJson;
    private String sqlQuery;
    private String requiredPermission;
    private boolean systemReport;
    private boolean active;
    private String createdBy;

    public void update(String name, String description, String columnsJson, String filtersJson, String sqlQuery) {
        this.name = name;
        this.description = description;
        this.columnsJson = columnsJson;
        this.filtersJson = filtersJson;
        this.sqlQuery = sqlQuery;
    }

    public void deactivate() {
        if (systemReport) {
            throw new IllegalStateException("System reports cannot be deactivated");
        }
        this.active = false;
    }

    public void activate() {
        this.active = true;
    }
}

