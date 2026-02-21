package tr.kontas.erp.app.crm.dtos;

import java.math.BigDecimal;

public record OpportunityPayload(
        String id,
        String companyId,
        String opportunityNumber,
        String title,
        String contactId,
        String leadId,
        String ownerId,
        String stage,
        BigDecimal probability,
        BigDecimal expectedValue,
        String currencyCode,
        String expectedCloseDate,
        String salesOrderId,
        String lostReason,
        String notes
) {}

