package tr.kontas.erp.app.reporting.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SavedReportPayload {
    private String id;
    private String reportDefinitionId;
    private String name;
    private String savedFiltersJson;
    private String savedSortsJson;
    private boolean shared;
    private String createdBy;
    private String createdAt;
}

