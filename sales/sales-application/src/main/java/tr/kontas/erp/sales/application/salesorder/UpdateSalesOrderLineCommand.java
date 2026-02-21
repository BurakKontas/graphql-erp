package tr.kontas.erp.sales.application.salesorder;

import java.math.BigDecimal;

public record UpdateSalesOrderLineCommand(
        String orderId,
        String lineId,
        BigDecimal quantity,
        BigDecimal unitPrice
) {
}
