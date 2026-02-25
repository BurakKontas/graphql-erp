package tr.kontas.erp.app.crm.dtos;

import tr.kontas.fluentvalidation.annotations.Validate;
import tr.kontas.fluentvalidation.interfaces.Validatable;
import tr.kontas.erp.app.crm.validators.CreateActivityInputValidator;

@Validate(validator = CreateActivityInputValidator.class)
public record CreateActivityInput(
        String companyId,
        String activityType,
        String subject,
        String ownerId,
        String relatedEntityType,
        String relatedEntityId,
        String scheduledAt,
        String description
) implements Validatable {}
