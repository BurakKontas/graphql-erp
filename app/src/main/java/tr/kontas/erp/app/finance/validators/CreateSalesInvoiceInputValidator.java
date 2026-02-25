package tr.kontas.erp.app.finance.validators;

import tr.kontas.erp.app.finance.dtos.CreateSalesInvoiceInput;
import tr.kontas.fluentvalidation.validation.Validator;

public class CreateSalesInvoiceInputValidator extends Validator<CreateSalesInvoiceInput> {
    public CreateSalesInvoiceInputValidator() {
        ruleFor(CreateSalesInvoiceInput::getCompanyId).notNull().notBlank();
        ruleFor(CreateSalesInvoiceInput::getInvoiceDate).notNull().notBlank();
        ruleFor(CreateSalesInvoiceInput::getLines).notNull().notEmpty().withMessage("At least one line is required");
    }
}
