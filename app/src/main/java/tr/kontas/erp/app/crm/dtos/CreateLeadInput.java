package tr.kontas.erp.app.crm.dtos;

import java.math.BigDecimal;
import tr.kontas.fluentvalidation.annotations.Validate;
import tr.kontas.fluentvalidation.interfaces.Validatable;
import tr.kontas.erp.app.crm.validators.CreateLeadInputValidator;

@Validate(validator = CreateLeadInputValidator.class)
public record CreateLeadInput(
        String companyId,
        String title,
        String contactId,
        String ownerId,
        String source,
        BigDecimal estimatedValue,
        String notes,
        String expectedCloseDate
) implements Validatable {}
