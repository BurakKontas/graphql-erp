package tr.kontas.erp.app.notification.dtos;

import lombok.Data;
import tr.kontas.fluentvalidation.annotations.Validate;
import tr.kontas.fluentvalidation.interfaces.Validatable;
import tr.kontas.erp.app.notification.validators.CreateNotificationConfigInputValidator;

import java.util.List;

@Data
@Validate(validator = CreateNotificationConfigInputValidator.class)
public class CreateNotificationConfigInput implements Validatable {
    private String companyId;
    private String eventType;
    private String notificationKey;
    private String description;
    private List<String> enabledChannels;
    private String deliveryTiming;
    private String cronExpression;
    private List<String> recipientRoles;
    private Boolean userOverridable;
}
