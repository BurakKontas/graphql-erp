package tr.kontas.erp.app.notification.validators;

import tr.kontas.erp.app.notification.dtos.CreateNotificationConfigInput;
import tr.kontas.fluentvalidation.validation.Validator;

public class CreateNotificationConfigInputValidator extends Validator<CreateNotificationConfigInput> {
    public CreateNotificationConfigInputValidator() {
        ruleFor(CreateNotificationConfigInput::getCompanyId).notNull().notBlank();
        ruleFor(CreateNotificationConfigInput::getEventType).notNull().notBlank();
    }
}
