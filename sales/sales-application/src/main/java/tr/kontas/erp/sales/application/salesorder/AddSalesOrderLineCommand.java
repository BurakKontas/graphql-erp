package tr.kontas.erp.sales.application.salesorder;

import java.math.BigDecimal;

public record AddSalesOrderLineCommand(
        String orderId,
        String itemId,
        String itemDescription,
        String unitCode,
        BigDecimal quantity,
        BigDecimal unitPrice,
        String taxCode
) {
}
