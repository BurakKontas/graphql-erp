package tr.kontas.erp.app.reporting.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tr.kontas.fluentvalidation.annotations.Validate;
import tr.kontas.fluentvalidation.interfaces.Validatable;
import tr.kontas.erp.app.reporting.validators.RunReportInputValidator;

@Getter
@Setter
@NoArgsConstructor
@Validate(validator = RunReportInputValidator.class)
public class RunReportInput implements Validatable {
    private String reportDefinitionId;
    private String parameters;
    private String format;
    private Integer page;
    private Integer size;
}
