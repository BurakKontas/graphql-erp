package tr.kontas.erp.app.reference.validators;

import tr.kontas.erp.app.reference.dtos.UpdateTaxRateInput;
import tr.kontas.fluentvalidation.validation.Validator;

public class UpdateTaxRateInputValidator extends Validator<UpdateTaxRateInput> {
    public UpdateTaxRateInputValidator() {
        ruleFor(UpdateTaxRateInput::getCompanyId).notNull().notBlank();
        ruleFor(UpdateTaxRateInput::getTaxCode).notNull().notBlank();
        ruleFor(UpdateTaxRateInput::getNewRate).notNull();
    }
}
