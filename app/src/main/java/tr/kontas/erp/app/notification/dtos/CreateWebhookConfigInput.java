package tr.kontas.erp.app.notification.dtos;

import lombok.Data;
import tr.kontas.fluentvalidation.annotations.Validate;
import tr.kontas.fluentvalidation.interfaces.Validatable;
import tr.kontas.erp.app.notification.validators.CreateWebhookConfigInputValidator;

import java.util.List;

@Data
@Validate(validator = CreateWebhookConfigInputValidator.class)
public class CreateWebhookConfigInput implements Validatable {
    private String companyId;
    private String targetUrl;
    private String secretKey;
    private List<String> eventTypes;
}
