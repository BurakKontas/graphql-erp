package tr.kontas.erp.app.crm.dtos;

import java.math.BigDecimal;
import tr.kontas.fluentvalidation.annotations.Validate;
import tr.kontas.fluentvalidation.interfaces.Validatable;
import tr.kontas.erp.app.crm.validators.CreateOpportunityInputValidator;

@Validate(validator = CreateOpportunityInputValidator.class)
public record CreateOpportunityInput(
        String companyId,
        String title,
        String contactId,
        String leadId,
        String ownerId,
        BigDecimal expectedValue,
        String currencyCode,
        String expectedCloseDate,
        String notes
) implements Validatable {}
