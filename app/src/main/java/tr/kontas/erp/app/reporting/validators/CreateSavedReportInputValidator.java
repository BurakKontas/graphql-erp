package tr.kontas.erp.app.reporting.validators;

import tr.kontas.erp.app.reporting.dtos.CreateSavedReportInput;
import tr.kontas.fluentvalidation.validation.Validator;

public class CreateSavedReportInputValidator extends Validator<CreateSavedReportInput> {
    public CreateSavedReportInputValidator() {
        ruleFor(CreateSavedReportInput::getReportDefinitionId).notNull().notBlank();
        ruleFor(CreateSavedReportInput::getName).notNull().notBlank();
    }
}
