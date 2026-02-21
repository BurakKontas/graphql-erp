package tr.kontas.erp.sales.domain.exception;

import tr.kontas.erp.sales.domain.salesorder.SalesOrderStatus;

public class InvalidSalesOrderStateException extends SalesDomainException {

    public InvalidSalesOrderStateException(SalesOrderStatus current, String operation) {
        super("Cannot perform '%s' on a SalesOrder in status '%s'".formatted(operation, current));
    }

    public InvalidSalesOrderStateException(SalesOrderStatus from, SalesOrderStatus to) {
        super("Invalid status transition: %s → %s".formatted(from, to));
    }
}
