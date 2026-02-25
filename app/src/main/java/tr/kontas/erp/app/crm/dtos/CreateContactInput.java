package tr.kontas.erp.app.crm.dtos;

import tr.kontas.fluentvalidation.annotations.Validate;
import tr.kontas.fluentvalidation.interfaces.Validatable;
import tr.kontas.erp.app.crm.validators.CreateContactInputValidator;

@Validate(validator = CreateContactInputValidator.class)
public record CreateContactInput(
        String companyId,
        String contactType,
        String firstName,
        String lastName,
        String companyName,
        String jobTitle,
        String email,
        String phone,
        String website,
        String address,
        String ownerId,
        String source,
        String notes
) implements Validatable {}
