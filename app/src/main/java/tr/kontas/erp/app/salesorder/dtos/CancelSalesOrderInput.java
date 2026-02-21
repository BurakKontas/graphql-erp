package tr.kontas.erp.app.salesorder.dtos;

import lombok.Data;

@Data
public class CancelSalesOrderInput {
    private String orderId;
    private String reason;
}
