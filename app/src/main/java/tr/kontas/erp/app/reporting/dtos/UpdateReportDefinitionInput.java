package tr.kontas.erp.app.reporting.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateReportDefinitionInput {
    private String definitionId;
    private String name;
    private String description;
    private String columnsJson;
    private String filtersJson;
}

