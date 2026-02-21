package tr.kontas.erp.finance.domain.salesinvoice;

import tr.kontas.erp.core.kernel.domain.model.Identifier;

import java.util.UUID;

public class SalesInvoiceLineId extends Identifier {

    private SalesInvoiceLineId(UUID value) {
        super(value);
    }

    public static SalesInvoiceLineId newId() {
        return new SalesInvoiceLineId(UUID.randomUUID());
    }

    public static SalesInvoiceLineId of(UUID value) {
        return new SalesInvoiceLineId(value);
    }

    public static SalesInvoiceLineId of(String value) {
        return new SalesInvoiceLineId(UUID.fromString(value));
    }

    public UUID asUUID() {
        return (UUID) getValue();
    }
}

