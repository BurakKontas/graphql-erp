package tr.kontas.erp.app.finance.validators;

import tr.kontas.erp.app.finance.dtos.CreateCreditNoteInput;
import tr.kontas.fluentvalidation.validation.Validator;

public class CreateCreditNoteInputValidator extends Validator<CreateCreditNoteInput> {
    public CreateCreditNoteInputValidator() {
        ruleFor(CreateCreditNoteInput::getCompanyId).notNull().notBlank();
        ruleFor(CreateCreditNoteInput::getCreditNoteDate).notNull().notBlank();
        ruleFor(CreateCreditNoteInput::getLines).notNull().notEmpty().withMessage("At least one credit note line is required");
    }
}
