package tr.kontas.erp.app.salesorder.dtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateSalesOrderLineInput {
    private String orderId;
    private String lineId;
    private BigDecimal quantity;
    private BigDecimal unitPrice;
}
