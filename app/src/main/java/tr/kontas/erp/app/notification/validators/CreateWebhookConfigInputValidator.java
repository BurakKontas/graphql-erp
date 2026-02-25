package tr.kontas.erp.app.notification.validators;

import tr.kontas.erp.app.notification.dtos.CreateWebhookConfigInput;
import tr.kontas.fluentvalidation.validation.Validator;

public class CreateWebhookConfigInputValidator extends Validator<CreateWebhookConfigInput> {
    public CreateWebhookConfigInputValidator() {
        ruleFor(CreateWebhookConfigInput::getCompanyId).notNull().notBlank();
        ruleFor(CreateWebhookConfigInput::getTargetUrl).notNull().notBlank();
    }
}
