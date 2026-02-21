package tr.kontas.erp.purchase.domain.vendorinvoice;

import tr.kontas.erp.core.kernel.domain.model.Identifier;
import java.util.UUID;

public class VendorInvoiceLineId extends Identifier {
    private VendorInvoiceLineId(UUID value) {
        super(value);
    }

    public static VendorInvoiceLineId newId() {
        return new VendorInvoiceLineId(UUID.randomUUID());
    }

    public static VendorInvoiceLineId of(UUID value) {
        return new VendorInvoiceLineId(value);
    }

    public static VendorInvoiceLineId of(String value) {
        return new VendorInvoiceLineId(UUID.fromString(value));
    }

    public UUID asUUID() {
        return (UUID) getValue();
    }
}
