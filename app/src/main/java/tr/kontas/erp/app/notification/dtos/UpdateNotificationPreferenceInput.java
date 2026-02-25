package tr.kontas.erp.app.notification.dtos;

import lombok.Data;
import tr.kontas.fluentvalidation.annotations.Validate;
import tr.kontas.fluentvalidation.interfaces.Validatable;
import tr.kontas.erp.app.notification.validators.UpdateNotificationPreferenceInputValidator;

import java.util.List;

@Data
@Validate(validator = UpdateNotificationPreferenceInputValidator.class)
public class UpdateNotificationPreferenceInput implements Validatable {
    private String preferenceId;
    private List<String> disabledChannels;
    private String preferredLocale;
}
