package tr.kontas.erp.app.crm.dtos;

import java.math.BigDecimal;
import tr.kontas.fluentvalidation.annotations.Validate;
import tr.kontas.fluentvalidation.interfaces.Validatable;
import tr.kontas.erp.app.crm.validators.CreateQuoteInputValidator;

@Validate(validator = CreateQuoteInputValidator.class)
public record CreateQuoteInput(
        String companyId,
        String opportunityId,
        String contactId,
        String ownerId,
        String quoteDate,
        String expiryDate,
        String currencyCode,
        String paymentTermCode,
        BigDecimal discountRate,
        String notes
) implements Validatable {}
