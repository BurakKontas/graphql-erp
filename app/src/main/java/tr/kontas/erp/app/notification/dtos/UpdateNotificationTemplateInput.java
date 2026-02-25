package tr.kontas.erp.app.notification.dtos;

import lombok.Data;
import tr.kontas.fluentvalidation.annotations.Validate;
import tr.kontas.fluentvalidation.interfaces.Validatable;
import tr.kontas.erp.app.notification.validators.UpdateNotificationTemplateInputValidator;

@Data
@Validate(validator = UpdateNotificationTemplateInputValidator.class)
public class UpdateNotificationTemplateInput implements Validatable {
    private String templateId;
    private String subject;
    private String bodyTemplate;
}
