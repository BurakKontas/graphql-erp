package tr.kontas.erp.app.crm.dtos;

import java.math.BigDecimal;

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
) {}

