package tr.kontas.erp.app.reporting.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tr.kontas.fluentvalidation.annotations.Validate;
import tr.kontas.fluentvalidation.interfaces.Validatable;
import tr.kontas.erp.app.reporting.validators.CreateReportDefinitionInputValidator;

@Getter
@Setter
@NoArgsConstructor
@Validate(validator = CreateReportDefinitionInputValidator.class)
public class CreateReportDefinitionInput implements Validatable {
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
