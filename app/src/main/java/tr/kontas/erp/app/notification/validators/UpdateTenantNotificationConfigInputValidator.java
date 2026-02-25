package tr.kontas.erp.app.notification.validators;

import tr.kontas.erp.app.notification.dtos.UpdateTenantNotificationConfigInput;
import tr.kontas.fluentvalidation.validation.Validator;

public class UpdateTenantNotificationConfigInputValidator extends Validator<UpdateTenantNotificationConfigInput> {
    public UpdateTenantNotificationConfigInputValidator() {
        ruleFor(UpdateTenantNotificationConfigInput::isAllNotificationsEnabled).notNull();
    }
}
