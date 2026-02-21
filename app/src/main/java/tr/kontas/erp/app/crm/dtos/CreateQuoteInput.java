package tr.kontas.erp.app.crm.dtos;

import java.math.BigDecimal;

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
) {}

