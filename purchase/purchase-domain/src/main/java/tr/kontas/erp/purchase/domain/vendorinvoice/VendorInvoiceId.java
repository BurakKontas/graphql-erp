package tr.kontas.erp.purchase.domain.vendorinvoice;

import tr.kontas.erp.core.kernel.domain.model.Identifier;
import java.util.UUID;

public class VendorInvoiceId extends Identifier {
    private VendorInvoiceId(UUID value) {
        super(value);
    }

    public static VendorInvoiceId newId() {
        return new VendorInvoiceId(UUID.randomUUID());
    }

    public static VendorInvoiceId of(UUID value) {
        return new VendorInvoiceId(value);
    }

    public static VendorInvoiceId of(String value) {
        return new VendorInvoiceId(UUID.fromString(value));
    }

    public UUID asUUID() {
        return (UUID) getValue();
    }
}
