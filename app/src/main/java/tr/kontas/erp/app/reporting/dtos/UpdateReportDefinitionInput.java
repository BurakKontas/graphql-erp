package tr.kontas.erp.app.reporting.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tr.kontas.fluentvalidation.annotations.Validate;
import tr.kontas.fluentvalidation.interfaces.Validatable;
import tr.kontas.erp.app.reporting.validators.UpdateReportDefinitionInputValidator;

@Getter
@Setter
@NoArgsConstructor
@Validate(validator = UpdateReportDefinitionInputValidator.class)
public class UpdateReportDefinitionInput implements Validatable {
    private String definitionId;
    private String name;
    private String description;
    private String columnsJson;
    private String filtersJson;
}
