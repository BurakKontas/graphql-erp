package tr.kontas.erp.app.reporting.validators;

import tr.kontas.erp.app.reporting.dtos.RunReportInput;
import tr.kontas.fluentvalidation.validation.Validator;

public class RunReportInputValidator extends Validator<RunReportInput> {
    public RunReportInputValidator() {
        ruleFor(RunReportInput::getReportDefinitionId).notNull().notBlank();
    }
}
