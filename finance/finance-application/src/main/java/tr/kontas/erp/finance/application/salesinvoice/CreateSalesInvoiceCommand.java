package tr.kontas.erp.finance.application.salesinvoice;

import tr.kontas.erp.core.domain.company.CompanyId;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record CreateSalesInvoiceCommand(
        CompanyId companyId,
        String invoiceType,
        String salesOrderId,
        String salesOrderNumber,
        String customerId,
        String customerName,
        LocalDate invoiceDate,
        LocalDate dueDate,
        String currencyCode,
        BigDecimal exchangeRate,
        List<LineInput> lines
) {
    public record LineInput(
            String salesOrderLineId,
            String itemId,
            String itemDescription,
            String unitCode,
            BigDecimal quantity,
            BigDecimal unitPrice,
            String taxCode,
            BigDecimal taxRate,
            String revenueAccountId
    ) {}
}

