package tr.kontas.erp.app.crm.dtos;

import java.math.BigDecimal;

public record QuoteLinePayload(
        String id,
        String itemId,
        String itemDescription,
        String unitCode,
        BigDecimal quantity,
        BigDecimal unitPrice,
        BigDecimal discountRate,
        String taxCode,
        BigDecimal taxRate,
        BigDecimal lineTotal,
        BigDecimal taxAmount,
        BigDecimal lineTotalWithTax
) {}

