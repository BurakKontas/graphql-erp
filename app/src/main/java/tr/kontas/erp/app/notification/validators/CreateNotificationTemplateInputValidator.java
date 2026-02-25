package tr.kontas.erp.app.notification.validators;

import tr.kontas.erp.app.notification.dtos.CreateNotificationTemplateInput;
import tr.kontas.fluentvalidation.validation.Validator;

public class CreateNotificationTemplateInputValidator extends Validator<CreateNotificationTemplateInput> {
    public CreateNotificationTemplateInputValidator() {
        ruleFor(CreateNotificationTemplateInput::getNotificationKey).notNull().notBlank();
        ruleFor(CreateNotificationTemplateInput::getChannel).notNull().notBlank();
        ruleFor(CreateNotificationTemplateInput::getLocale).notNull().notBlank();
    }
}
