package tr.kontas.erp.app.notification.validators;

import tr.kontas.erp.app.notification.dtos.CreateNotificationPreferenceInput;
import tr.kontas.fluentvalidation.validation.Validator;

public class CreateNotificationPreferenceInputValidator extends Validator<CreateNotificationPreferenceInput> {
    public CreateNotificationPreferenceInputValidator() {
        ruleFor(CreateNotificationPreferenceInput::getUserId).notNull().notBlank();
        ruleFor(CreateNotificationPreferenceInput::getNotificationKey).notNull().notBlank();
    }
}
