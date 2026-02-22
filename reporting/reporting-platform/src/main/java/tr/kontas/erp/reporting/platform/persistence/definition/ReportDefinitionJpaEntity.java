package tr.kontas.erp.reporting.platform.persistence.definition;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "report_definitions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportDefinitionJpaEntity {

    @Id
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private String module;

    @Column(nullable = false)
    private String type;

    @Column(name = "data_source")
    private String dataSource;

    @Column(name = "columns_json", columnDefinition = "TEXT")
    private String columnsJson;

    @Column(name = "filters_json", columnDefinition = "TEXT")
    private String filtersJson;

    @Column(name = "sql_query", columnDefinition = "TEXT")
    private String sqlQuery;

    @Column(name = "required_permission")
    private String requiredPermission;

    @Column(name = "system_report", nullable = false)
    private boolean systemReport;

    @Column(nullable = false)
    private boolean active;

    @Column(name = "created_by")
    private String createdBy;
}

