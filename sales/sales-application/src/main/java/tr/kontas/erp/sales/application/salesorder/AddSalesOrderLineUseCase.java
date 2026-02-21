package tr.kontas.erp.sales.application.salesorder;

import tr.kontas.erp.sales.domain.salesorder.SalesOrderLineId;

public interface AddSalesOrderLineUseCase {
    SalesOrderLineId execute(AddSalesOrderLineCommand command);
}
