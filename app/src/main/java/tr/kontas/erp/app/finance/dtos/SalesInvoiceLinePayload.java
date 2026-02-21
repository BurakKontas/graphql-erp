package tr.kontas.erp.app.finance.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class SalesInvoiceLinePayload {
    private String id;
    private String salesOrderLineId;
    private String itemId;
    private String itemDescription;
    private String unitCode;
    private BigDecimal quantity;
    private BigDecimal unitPrice;
    private String taxCode;
    private BigDecimal taxRate;
    private BigDecimal lineTotal;
    private BigDecimal taxAmount;
    private BigDecimal lineTotalWithTax;
    private String revenueAccountId;
}

