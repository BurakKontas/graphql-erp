package tr.kontas.erp.app.finance.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
public class SalesInvoicePayload {
    private String id;
    private String companyId;
    private String invoiceNumber;
    private String invoiceType;
    private String salesOrderId;
    private String salesOrderNumber;
    private String customerId;
    private String customerName;
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
    private List<SalesInvoiceLinePayload> lines;
}

