package tr.kontas.erp.app.crm.dtos;

import java.math.BigDecimal;

public record LeadPayload(
        String id,
        String companyId,
        String leadNumber,
        String title,
        String contactId,
        String ownerId,
        String status,
        String source,
        BigDecimal estimatedValue,
        String disqualificationReason,
        String notes,
        String expectedCloseDate,
        String opportunityId
) {}

