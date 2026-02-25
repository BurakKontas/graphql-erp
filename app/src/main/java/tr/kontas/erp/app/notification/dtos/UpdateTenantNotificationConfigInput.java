package tr.kontas.erp.app.notification.dtos;

import lombok.Data;
import tr.kontas.fluentvalidation.annotations.Validate;
import tr.kontas.fluentvalidation.interfaces.Validatable;
import tr.kontas.erp.app.notification.validators.UpdateTenantNotificationConfigInputValidator;

@Data
@Validate(validator = UpdateTenantNotificationConfigInputValidator.class)
public class UpdateTenantNotificationConfigInput implements Validatable {
    private boolean allNotificationsEnabled;
    private String disabledReason;
    private String disabledUntil;
}
