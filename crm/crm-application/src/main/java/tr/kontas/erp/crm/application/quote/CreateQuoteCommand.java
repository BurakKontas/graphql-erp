package tr.kontas.erp.crm.application.quote;

import tr.kontas.erp.core.domain.company.CompanyId;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateQuoteCommand(
        CompanyId companyId,
        String opportunityId,
        String contactId,
        String ownerId,
        LocalDate quoteDate,
        LocalDate expiryDate,
        String currencyCode,
        String paymentTermCode,
        BigDecimal discountRate,
        String notes
) {}

