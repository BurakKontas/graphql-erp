package tr.kontas.erp.app.crm.dtos;

import java.math.BigDecimal;
import java.util.List;

public record QuotePayload(
        String id,
        String companyId,
        String quoteNumber,
        String opportunityId,
        String contactId,
        String ownerId,
        String quoteDate,
        String expiryDate,
        String currencyCode,
        String paymentTermCode,
        String status,
        String version,
        String previousQuoteId,
        BigDecimal subtotal,
        BigDecimal taxTotal,
        BigDecimal total,
        BigDecimal discountRate,
        String notes,
        List<QuoteLinePayload> lines
) {}

