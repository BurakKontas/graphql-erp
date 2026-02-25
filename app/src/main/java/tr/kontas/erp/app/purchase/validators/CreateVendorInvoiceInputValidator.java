package tr.kontas.erp.app.purchase.validators;

import tr.kontas.erp.app.purchase.dtos.CreateVendorInvoiceInput;
import tr.kontas.fluentvalidation.validation.Validator;

public class CreateVendorInvoiceInputValidator extends Validator<CreateVendorInvoiceInput> {
    public CreateVendorInvoiceInputValidator() {
        ruleFor(CreateVendorInvoiceInput::getCompanyId).notNull().notBlank();
        ruleFor(CreateVendorInvoiceInput::getVendorId).notNull().notBlank();
        ruleFor(CreateVendorInvoiceInput::getLines).notNull().notEmpty().withMessage("At least one line is required");
    }
}
