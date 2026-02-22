package tr.kontas.erp.app.reporting.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateSavedReportInput {
    private String reportDefinitionId;
    private String name;
    private String savedFiltersJson;
    private String savedSortsJson;
    private Boolean shared;
}

