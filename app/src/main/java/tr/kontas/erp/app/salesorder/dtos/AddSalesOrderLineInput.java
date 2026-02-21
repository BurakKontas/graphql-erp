package tr.kontas.erp.app.salesorder.dtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AddSalesOrderLineInput {
    private String orderId;
    private String itemId;
    private String itemDescription;
    private String unitCode;
    private BigDecimal quantity;
    private BigDecimal unitPrice;
    private String taxCode;
}
