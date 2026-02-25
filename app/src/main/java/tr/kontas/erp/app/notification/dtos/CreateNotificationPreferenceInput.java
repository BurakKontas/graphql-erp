package tr.kontas.erp.app.notification.dtos;

import lombok.Data;
import tr.kontas.fluentvalidation.annotations.Validate;
import tr.kontas.fluentvalidation.interfaces.Validatable;
import tr.kontas.erp.app.notification.validators.CreateNotificationPreferenceInputValidator;

import java.util.List;

@Data
@Validate(validator = CreateNotificationPreferenceInputValidator.class)
public class CreateNotificationPreferenceInput implements Validatable {
    private String userId;
    private String notificationKey;
    private List<String> disabledChannels;
    private String preferredLocale;
}
