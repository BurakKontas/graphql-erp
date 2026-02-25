package tr.kontas.erp.app.reporting.validators;

import tr.kontas.erp.app.reporting.dtos.CreateScheduledReportInput;
import tr.kontas.fluentvalidation.validation.Validator;

public class CreateScheduledReportInputValidator extends Validator<CreateScheduledReportInput> {
    public CreateScheduledReportInputValidator() {
        ruleFor(CreateScheduledReportInput::getReportDefinitionId).notNull().notBlank();
        ruleFor(CreateScheduledReportInput::getName).notNull().notBlank();
        ruleFor(CreateScheduledReportInput::getCronExpression).notNull().notBlank();
    }
}
