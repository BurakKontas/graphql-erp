package tr.kontas.erp.app.purchase.validators;

import tr.kontas.erp.app.purchase.dtos.CreateVendorCatalogInput;
import tr.kontas.fluentvalidation.validation.Validator;

public class CreateVendorCatalogInputValidator extends Validator<CreateVendorCatalogInput> {
    public CreateVendorCatalogInputValidator() {
        ruleFor(CreateVendorCatalogInput::getCompanyId).notNull().notBlank();
        ruleFor(CreateVendorCatalogInput::getVendorId).notNull().notBlank();
        ruleFor(CreateVendorCatalogInput::getEntries).notNull().notEmpty();
    }
}
