package tr.kontas.erp.sales.application.salesorder;

import tr.kontas.erp.sales.domain.salesorder.SalesOrder;
import tr.kontas.erp.sales.domain.salesorder.SalesOrderId;

import java.util.List;

public interface GetSalesOrdersByIdsUseCase {
    List<SalesOrder> execute(List<SalesOrderId> ids);
}
