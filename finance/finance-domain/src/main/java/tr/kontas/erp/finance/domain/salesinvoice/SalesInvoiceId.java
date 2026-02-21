package tr.kontas.erp.finance.domain.salesinvoice;

import tr.kontas.erp.core.kernel.domain.model.Identifier;

import java.util.UUID;

public class SalesInvoiceId extends Identifier {

    private SalesInvoiceId(UUID value) {
        super(value);
    }

    public static SalesInvoiceId newId() {
        return new SalesInvoiceId(UUID.randomUUID());
    }

    public static SalesInvoiceId of(UUID value) {
        return new SalesInvoiceId(value);
    }

    public static SalesInvoiceId of(String value) {
        return new SalesInvoiceId(UUID.fromString(value));
    }

    public UUID asUUID() {
        return (UUID) getValue();
    }
}

