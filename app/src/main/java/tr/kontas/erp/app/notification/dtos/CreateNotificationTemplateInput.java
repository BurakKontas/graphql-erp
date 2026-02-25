package tr.kontas.erp.app.notification.dtos;

import lombok.Data;
import tr.kontas.fluentvalidation.annotations.Validate;
import tr.kontas.fluentvalidation.interfaces.Validatable;
import tr.kontas.erp.app.notification.validators.CreateNotificationTemplateInputValidator;

@Data
@Validate(validator = CreateNotificationTemplateInputValidator.class)
public class CreateNotificationTemplateInput implements Validatable {
    private String notificationKey;
    private String channel;
    private String locale;
    private String subject;
    private String bodyTemplate;
    private Boolean systemTemplate;
}
