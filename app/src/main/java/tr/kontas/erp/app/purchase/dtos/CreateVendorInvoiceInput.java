package tr.kontas.erp.app.purchase.dtos;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CreateVendorInvoiceInput {
    private String companyId;
    private String vendorInvoiceRef;
    private String purchaseOrderId;
    private String vendorId;
    private String vendorName;
    private String accountingPeriodId;
    private String invoiceDate;
    private String dueDate;
    private String currencyCode;
    private BigDecimal exchangeRate;
    private List<LineInput> lines;

    @Data
    public static class LineInput {
        private String poLineId;
        private String itemId;
        private String itemDescription;
        private String unitCode;
        private BigDecimal quantity;
        private BigDecimal unitPrice;
        private String taxCode;
        private BigDecimal taxRate;
        private String accountId;
    }
}

