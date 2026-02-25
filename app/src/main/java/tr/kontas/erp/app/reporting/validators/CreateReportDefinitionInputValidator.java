package tr.kontas.erp.app.reporting.validators;

import tr.kontas.erp.app.reporting.dtos.CreateReportDefinitionInput;
import tr.kontas.fluentvalidation.validation.Validator;

public class CreateReportDefinitionInputValidator extends Validator<CreateReportDefinitionInput> {
    public CreateReportDefinitionInputValidator() {
        ruleFor(CreateReportDefinitionInput::getName).notNull().notBlank();
        ruleFor(CreateReportDefinitionInput::getModule).notNull().notBlank();
        ruleFor(CreateReportDefinitionInput::getType).notNull().notBlank();
        ruleFor(CreateReportDefinitionInput::getDataSource).notNull().notBlank();
    }
}
