package tr.kontas.erp.sales.application.salesorder;

import tr.kontas.erp.sales.domain.salesorder.SalesOrderId;

public interface CreateSalesOrderUseCase {
    SalesOrderId execute(CreateSalesOrderCommand command);
}
