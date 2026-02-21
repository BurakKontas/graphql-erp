package tr.kontas.erp.app.crm.dtos;

import java.math.BigDecimal;

public record CreateLeadInput(
        String companyId,
        String title,
        String contactId,
        String ownerId,
        String source,
        BigDecimal estimatedValue,
        String notes,
        String expectedCloseDate
) {}

