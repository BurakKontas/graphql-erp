package tr.kontas.erp.crm.application.opportunity;

import tr.kontas.erp.core.domain.company.CompanyId;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateOpportunityCommand(
        CompanyId companyId,
        String title,
        String contactId,
        String leadId,
        String ownerId,
        BigDecimal expectedValue,
        String currencyCode,
        LocalDate expectedCloseDate,
        String notes
) {}

