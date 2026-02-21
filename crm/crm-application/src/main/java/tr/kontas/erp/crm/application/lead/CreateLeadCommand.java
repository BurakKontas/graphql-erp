package tr.kontas.erp.crm.application.lead;

import tr.kontas.erp.core.domain.company.CompanyId;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateLeadCommand(
        CompanyId companyId,
        String title,
        String contactId,
        String ownerId,
        String source,
        BigDecimal estimatedValue,
        String notes,
        LocalDate expectedCloseDate
) {}

