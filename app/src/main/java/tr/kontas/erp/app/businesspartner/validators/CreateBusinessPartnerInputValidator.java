package tr.kontas.erp.app.businesspartner.validators;

import tr.kontas.erp.app.businesspartner.dtos.CreateBusinessPartnerInput;
import tr.kontas.fluentvalidation.validation.Validator;

public class CreateBusinessPartnerInputValidator extends Validator<CreateBusinessPartnerInput> {
    public CreateBusinessPartnerInputValidator() {
        ruleFor(CreateBusinessPartnerInput::getCompanyId).notNull().notBlank();
        ruleFor(CreateBusinessPartnerInput::getCode).notNull().notBlank();
        ruleFor(CreateBusinessPartnerInput::getName).notNull().notBlank();
    }
}
