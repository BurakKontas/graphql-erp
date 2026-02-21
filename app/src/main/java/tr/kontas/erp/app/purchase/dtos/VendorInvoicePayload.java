package tr.kontas.erp.app.purchase.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
public class VendorInvoicePayload {
    private String id;
    private String companyId;
    private String invoiceNumber;
    private String vendorInvoiceRef;
    private String purchaseOrderId;
    private String vendorId;
    private String vendorName;
    private String accountingPeriodId;
    private String invoiceDate;
    private String dueDate;
    private String currencyCode;
    private BigDecimal exchangeRate;
    private String status;
    private BigDecimal subtotal;
    private BigDecimal taxTotal;
    private BigDecimal total;
    private BigDecimal paidAmount;
    private BigDecimal remainingAmount;
    private List<VendorInvoiceLinePayload> lines;
}

