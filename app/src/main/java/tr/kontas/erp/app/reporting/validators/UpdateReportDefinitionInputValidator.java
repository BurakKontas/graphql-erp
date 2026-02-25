package tr.kontas.erp.app.reporting.validators;

import tr.kontas.erp.app.reporting.dtos.UpdateReportDefinitionInput;
import tr.kontas.fluentvalidation.validation.Validator;

public class UpdateReportDefinitionInputValidator extends Validator<UpdateReportDefinitionInput> {
    public UpdateReportDefinitionInputValidator() {
        ruleFor(UpdateReportDefinitionInput::getDefinitionId).notNull().notBlank();
        ruleFor(UpdateReportDefinitionInput::getName).notNull().notBlank();
    }
}
