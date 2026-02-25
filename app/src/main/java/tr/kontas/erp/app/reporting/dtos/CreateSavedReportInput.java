package tr.kontas.erp.app.reporting.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tr.kontas.fluentvalidation.annotations.Validate;
import tr.kontas.fluentvalidation.interfaces.Validatable;
import tr.kontas.erp.app.reporting.validators.CreateSavedReportInputValidator;

@Getter
@Setter
@NoArgsConstructor
@Validate(validator = CreateSavedReportInputValidator.class)
public class CreateSavedReportInput implements Validatable {
    private String reportDefinitionId;
    private String name;
    private String savedFiltersJson;
    private String savedSortsJson;
    private Boolean shared;
}
