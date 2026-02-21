package tr.kontas.erp.app.salesorder.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class SalesOrderLinePayload {
    private String id;
    private int sequence;
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
}
