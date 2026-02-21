package tr.kontas.erp.sales.domain.exception;

import tr.kontas.erp.sales.domain.salesorder.InvoicingStatus;

public class CannotCancelInvoicedOrderException extends SalesDomainException {

    public CannotCancelInvoicedOrderException(InvoicingStatus invoicingStatus) {
        super(("Cannot cancel a SalesOrder with invoicing status '%s'. "
                + "A credit note must be issued in the Finance module first.")
                .formatted(invoicingStatus));
    }
}

