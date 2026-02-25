package tr.kontas.erp.app.finance.validators;

import tr.kontas.erp.app.finance.dtos.CreateExpenseInput;
import tr.kontas.fluentvalidation.validation.Validator;

public class CreateExpenseInputValidator extends Validator<CreateExpenseInput> {
    public CreateExpenseInputValidator() {
        ruleFor(CreateExpenseInput::getCompanyId).notNull().notBlank();
        ruleFor(CreateExpenseInput::getAmount).notNull();
        ruleFor(CreateExpenseInput::getDescription).notNull().notBlank();
    }
}
