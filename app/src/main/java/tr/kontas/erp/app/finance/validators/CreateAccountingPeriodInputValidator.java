package tr.kontas.erp.app.finance.validators;

import tr.kontas.erp.app.finance.dtos.CreateAccountingPeriodInput;
import tr.kontas.fluentvalidation.validation.Validator;

public class CreateAccountingPeriodInputValidator extends Validator<CreateAccountingPeriodInput> {
    public CreateAccountingPeriodInputValidator() {
        ruleFor(CreateAccountingPeriodInput::getCompanyId).notNull().notBlank();
        ruleFor(CreateAccountingPeriodInput::getPeriodType).notNull().notBlank();
        ruleFor(CreateAccountingPeriodInput::getStartDate).notNull().notBlank();
        ruleFor(CreateAccountingPeriodInput::getEndDate).notNull().notBlank();
    }
}
