package tr.kontas.erp.app.finance.dtos;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CreateSalesInvoiceInput {
    private String companyId;
    private String invoiceType;
    private String salesOrderId;
    private String salesOrderNumber;
    private String customerId;
    private String customerName;
    private String invoiceDate;
    private String dueDate;
    private String currencyCode;
    private BigDecimal exchangeRate;
    private List<SalesInvoiceLineInput> lines;

    @Data
    public static class SalesInvoiceLineInput {
        private String salesOrderLineId;
        private String itemId;
        private String itemDescription;
        private String unitCode;
        private BigDecimal quantity;
        private BigDecimal unitPrice;
        private String taxCode;
        private BigDecimal taxRate;
        private String revenueAccountId;
    }
}

