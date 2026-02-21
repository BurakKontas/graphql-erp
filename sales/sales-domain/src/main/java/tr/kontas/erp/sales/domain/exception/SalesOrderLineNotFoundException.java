package tr.kontas.erp.sales.domain.exception;

import tr.kontas.erp.sales.domain.salesorder.SalesOrderLineId;

public class SalesOrderLineNotFoundException extends SalesDomainException {

    public SalesOrderLineNotFoundException(SalesOrderLineId lineId) {
        super("SalesOrderLine not found with id: " + lineId);
    }
}
