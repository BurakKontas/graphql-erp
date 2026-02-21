package tr.kontas.erp.purchase.application.vendorinvoice;

import tr.kontas.erp.core.domain.company.CompanyId;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record CreateVendorInvoiceCommand(
        CompanyId companyId,
        String vendorInvoiceRef,
        String purchaseOrderId,
        String vendorId,
        String vendorName,
        String accountingPeriodId,
        LocalDate invoiceDate,
        LocalDate dueDate,
        String currencyCode,
        BigDecimal exchangeRate,
        List<LineCommand> lines
) {
    public record LineCommand(
            String poLineId,
            String itemId,
            String itemDescription,
            String unitCode,
            BigDecimal quantity,
            BigDecimal unitPrice,
            String taxCode,
            BigDecimal taxRate,
            String accountId
    ) {
    }
}

