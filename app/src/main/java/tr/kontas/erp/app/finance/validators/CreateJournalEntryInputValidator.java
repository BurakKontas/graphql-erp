package tr.kontas.erp.app.finance.validators;

import tr.kontas.erp.app.finance.dtos.CreateJournalEntryInput;
import tr.kontas.fluentvalidation.validation.Validator;

public class CreateJournalEntryInputValidator extends Validator<CreateJournalEntryInput> {
    public CreateJournalEntryInputValidator() {
        ruleFor(CreateJournalEntryInput::getCompanyId).notNull().notBlank();
        ruleFor(CreateJournalEntryInput::getEntryDate).notNull().notBlank();
        ruleFor(CreateJournalEntryInput::getLines).notNull().notEmpty().withMessage("At least one line is required");
    }
}
