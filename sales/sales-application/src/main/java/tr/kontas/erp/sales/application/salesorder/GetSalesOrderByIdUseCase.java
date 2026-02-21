package tr.kontas.erp.sales.application.salesorder;

import tr.kontas.erp.sales.domain.salesorder.SalesOrder;
import tr.kontas.erp.sales.domain.salesorder.SalesOrderId;

public interface GetSalesOrderByIdUseCase {
    SalesOrder execute(SalesOrderId id);
}
