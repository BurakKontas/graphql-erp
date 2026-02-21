package tr.kontas.erp.app.purchase.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class PurchaseOrderLinePayload {
    private String id;
    private String requestLineId;
    private String itemId;
    private String itemDescription;
    private String unitCode;
    private BigDecimal orderedQty;
    private BigDecimal receivedQty;
    private BigDecimal remainingQty;
    private BigDecimal unitPrice;
    private String taxCode;
    private BigDecimal taxRate;
    private BigDecimal lineTotal;
    private BigDecimal taxAmount;
    private String expenseAccountId;
}

