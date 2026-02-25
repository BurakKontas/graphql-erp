package tr.kontas.erp.app.notification.validators;

import tr.kontas.erp.app.notification.dtos.UpdateNotificationPreferenceInput;
import tr.kontas.fluentvalidation.validation.Validator;

public class UpdateNotificationPreferenceInputValidator extends Validator<UpdateNotificationPreferenceInput> {
    public UpdateNotificationPreferenceInputValidator() {
        ruleFor(UpdateNotificationPreferenceInput::getPreferenceId).notNull().notBlank();
    }
}
