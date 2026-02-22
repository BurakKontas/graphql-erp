package tr.kontas.erp.reporting.platform.persistence.saved;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "saved_reports")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SavedReportJpaEntity {

    @Id
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;

    @Column(name = "report_definition_id", nullable = false)
    private UUID reportDefinitionId;

    @Column(nullable = false)
    private String name;

    @Column(name = "saved_filters_json", columnDefinition = "TEXT")
    private String savedFiltersJson;

    @Column(name = "saved_sorts_json", columnDefinition = "TEXT")
    private String savedSortsJson;

    @Column(nullable = false)
    private boolean shared;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_at")
    private Instant createdAt;
}

