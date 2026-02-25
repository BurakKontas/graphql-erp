package tr.kontas.erp.app.notification.validators;

import tr.kontas.erp.app.notification.dtos.UpdateNotificationTemplateInput;
import tr.kontas.fluentvalidation.validation.Validator;

public class UpdateNotificationTemplateInputValidator extends Validator<UpdateNotificationTemplateInput> {
    public UpdateNotificationTemplateInputValidator() {
        ruleFor(UpdateNotificationTemplateInput::getTemplateId).notNull().notBlank();
    }
}
