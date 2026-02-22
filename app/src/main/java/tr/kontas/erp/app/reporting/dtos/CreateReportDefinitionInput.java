package tr.kontas.erp.app.reporting.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateReportDefinitionInput {
    private String name;
    private String description;
    private String module;
    private String type;
    private String dataSource;
    private String columnsJson;
    private String filtersJson;
    private String requiredPermission;
    private Boolean systemReport;
}

